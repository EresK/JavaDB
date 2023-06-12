package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.controller.response.FlightResponse;
import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.domain.GroupedAmountEntity;
import edu.javadb.flightsspring.domain.TicketFlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.GroupedAmountRepository;
import edu.javadb.flightsspring.repos.TicketFlightsRepository;
import edu.javadb.flightsspring.service.util.FareConditions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class RoutesServiceNew {
    private final FlightsRepository flightsRepository;
    private final TicketFlightsRepository ticketFlightsRepository;
    private final GroupedAmountRepository groupedAmountRepository;

    @Transactional
    public Set<List<FlightResponse>> findRoutes(String departureAirport,
                                                String arrivalAirport,
                                                OffsetDateTime departureDate,
                                                FareConditions fareCondition,
                                                int maxConnections) {
        if (maxConnections < 1)
            throw new IllegalArgumentException("Max connections must be positive number");

        var flights = flightsRepository.findAllByStatusAndScheduledDepartureBetween("Scheduled", departureDate, departureDate.plusMonths(1));

//        System.out.println("Flights size " + flights.size());

        Set<List<FlightEntity>> foundRoutes = new HashSet<>();

        dfs(departureAirport, arrivalAirport,
                departureDate, null,
                maxConnections,
                flights, new ArrayList<>(),
                new HashSet<>(), foundRoutes);

        return filterByFareCondition(foundRoutes, fareCondition);
    }

    private Set<List<FlightResponse>> convertToFlightResponse(Set<List<FlightEntity>> foundRoutes,
                                                              FareConditions fareCondition,
                                                              List<GroupedAmountEntity> groupedAmount) {
        Set<List<FlightResponse>> responseRoutes = new HashSet<>();

        for (var route : foundRoutes) {
            var aNewRoute = new ArrayList<FlightResponse>();

            for (var flight : route) {
                Optional<GroupedAmountEntity> flightGroupAmount = groupedAmount
                        .stream()
                        .filter(ga ->
                                ga.getFlightNo().equalsIgnoreCase(flight.getFlightNo()) &&
                                        ga.getFareConditions().equalsIgnoreCase(fareCondition.toString()))
                        .findFirst();

                if (flightGroupAmount.isEmpty())
                    throw new IllegalStateException("Flight group amount not found for flight with id " + flight.getFlightId());

                aNewRoute.add(FlightResponse.fromEntity(flight,
                        fareCondition.toString(),
                        flightGroupAmount.get().getAmount()));
            }
            responseRoutes.add(aNewRoute);
        }
        return responseRoutes;
    }

    private Set<List<FlightResponse>> filterByFareCondition(Set<List<FlightEntity>> foundRoutes, FareConditions fareCondition) {
        List<Integer> listOfFlightIds = foundRoutes
                .stream()
                .map(r -> r
                        .stream()
                        .map(FlightEntity::getFlightId)
                        .distinct()
                        .toList())
                .flatMap(List::stream)
                .distinct()
                .toList();

        List<String> listOfFlightNos = foundRoutes
                .stream()
                .map(r -> r
                        .stream()
                        .map(FlightEntity::getFlightNo)
                        .distinct()
                        .toList())
                .flatMap(List::stream)
                .distinct()
                .toList();

        var groupedAmount = groupedAmountRepository.findAllByGroupedAmountKeyFlightNoIn(listOfFlightNos);
        var ticketFlights = ticketFlightsRepository.findAllByTicketFlightKeyFlightIdIn(listOfFlightIds);

//        System.out.println("Routes count before " + foundRoutes.size());

        foundRoutes.removeIf(r -> !hasSeatWithFareCondition(fareCondition, r, groupedAmount, ticketFlights));

//        System.out.println("Routes count after " + foundRoutes.size());

        return convertToFlightResponse(foundRoutes, fareCondition, groupedAmount);
    }

    private boolean hasSeatWithFareCondition(FareConditions fareCondition,
                                             List<FlightEntity> route,
                                             List<GroupedAmountEntity> groupedAmount,
                                             List<TicketFlightEntity> ticketFlights) {
        boolean hasSeats = true;

        for (var flight : route) {
            Optional<GroupedAmountEntity> flightGroupAmount = groupedAmount
                    .stream()
                    .filter(ga ->
                            ga.getFlightNo().equalsIgnoreCase(flight.getFlightNo()) &&
                                    ga.getFareConditions().equalsIgnoreCase(fareCondition.toString()))
                    .findFirst();

            // It's normal when a flight has no business or comfort class
            if (flightGroupAmount.isEmpty()) {
                hasSeats = false;
                break;
            }

            // It's looks like that it is a special flight
            if (flightGroupAmount.get().getAmount().compareTo(BigDecimal.valueOf(1L)) < 0) {
                hasSeats = false;
                break;
            }

            long issuedSeatsCount = ticketFlights
                    .stream()
                    .filter(tf ->
                            tf.getFlightId().equals(flight.getFlightId()) &&
                                    tf.getFareConditions().equalsIgnoreCase(fareCondition.toString()))
                    .count();

            if (issuedSeatsCount >= flightGroupAmount.get().getSeatCount()) {
                hasSeats = false;
                break;
            }
        }

        return hasSeats;
    }

    private void dfs(final String currentAirport,
                     final String arrivalAirport,
                     final OffsetDateTime departureDate,
                     final OffsetDateTime currentArrivalDate,
                     final int maxConnections,
                     final List<FlightEntity> flights,
                     final List<FlightEntity> currentRoute,
                     final Set<String> markedCities,
                     final Set<List<FlightEntity>> foundRoutes) {
        if (currentRoute.size() >= maxConnections)
            return;

        List<FlightEntity> currentFlights;

        if (currentRoute.size() == 0) {
            currentFlights = flights
                    .stream()
                    .filter(f -> f.getDepartureAirport().equalsIgnoreCase(currentAirport) &&
                            f.getScheduledDeparture().isAfter(departureDate) &&
                            f.getScheduledDeparture().isBefore(departureDate.plusDays(1)))
                    .toList();
        } else {
            currentFlights = flights.stream()
                    .filter(f -> f.getDepartureAirport().equalsIgnoreCase(currentAirport) &&
                            f.getScheduledDeparture().isAfter(currentArrivalDate))
                    .toList();
        }

        for (var f : currentFlights) {
            if (markedCities.contains(f.getArrivalCity()))
                continue;

            if (f.getArrivalAirport().equalsIgnoreCase(arrivalAirport)) {
                var route = new ArrayList<>(currentRoute);
                route.add(f);
                foundRoutes.add(route);
                continue;
            }

            markedCities.add(f.getDepartureCity());
            currentRoute.add(f);

            dfs(f.getArrivalAirport(), arrivalAirport,
                    departureDate, f.getScheduledArrival(),
                    maxConnections,
                    flights, currentRoute,
                    markedCities, foundRoutes);

            currentRoute.remove(f);
            markedCities.remove(f.getDepartureCity());
        }
    }
}
