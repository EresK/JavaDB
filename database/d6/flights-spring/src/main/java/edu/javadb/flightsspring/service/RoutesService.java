package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.domain.RouteEntity;
import edu.javadb.flightsspring.domain.SeatEntity;
import edu.javadb.flightsspring.domain.TicketFlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.RoutesRepository;
import edu.javadb.flightsspring.repos.SeatsRepository;
import edu.javadb.flightsspring.repos.TicketFlightsRepository;
import edu.javadb.flightsspring.service.util.FareConditions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoutesService {
    private final RoutesRepository routesRepository;
    private final FlightsRepository flightsRepository;
    private final TicketFlightsRepository ticketFlightsRepository;
    private final SeatsRepository seatsRepository;

    private Set<String> currentRoutesAirports;
    private ArrayList<FlightEntity> currentRoute;
    private Set<List<FlightEntity>> foundRoutes;

    @Transactional
    public Set<List<FlightEntity>> findAllRoutes(String departureAirport,
                                                 String arrivalAirport,
                                                 OffsetDateTime departureDate,
                                                 FareConditions fareCondition,
                                                 Integer additionalRoutes) {
        List<FlightEntity> flights = flightsRepository.findAllByStatus("Scheduled");

        flights = flights.stream()
                .filter(f -> departureDate.isEqual(f.getScheduledDeparture()) ||
                        departureDate.isBefore(f.getScheduledDeparture()))
                .toList();

        findRoutes(departureAirport, arrivalAirport, departureDate, flights);
        filterByAdditionalRoutes(additionalRoutes);
        filterByFareConditions(fareCondition);

        var currentFoundRoutes = Set.copyOf(foundRoutes);

        clearRoutesState();

        return currentFoundRoutes;
    }

    private void filterByAdditionalRoutes(Integer additionalRoutes) {
        if (additionalRoutes != null)
            foundRoutes.removeIf(list -> list.size() > (additionalRoutes + 1));
    }

    private void filterByFareConditions(FareConditions fareCondition) {
        if (fareCondition == null)
            return;

        var routes = routesRepository.findAll();
        var seats = seatsRepository.findAll();
        var ticketFlights = ticketFlightsRepository.findAllByFareConditions(fareCondition.toString());

        foundRoutes.removeIf(list -> !isRouteMatchedByFareCondition(fareCondition, list, routes, seats, ticketFlights));
    }

    private boolean isRouteMatchedByFareCondition(FareConditions fareCondition,
                                                  List<FlightEntity> aRoute,
                                                  List<RouteEntity> routes,
                                                  List<SeatEntity> seats,
                                                  List<TicketFlightEntity> ticketFlights) {
        boolean isMatched = true;

        for (var flight : aRoute) {
            // Route, который имеет тот же номер, что и текущий перелет
            Optional<RouteEntity> currentRoute = routes.stream()
                    .filter(r -> flight.getFlightNo().equalsIgnoreCase(r.getFlightNo()))
                    .findFirst();

            if (currentRoute.isEmpty())
                throw new IllegalStateException("No route is found for a flight: " + flight.getFlightId());

            // Все места, которые отностятся к самолету текущего Route'а и совпадают по классу обслуживания
            long currentSeatsCount = seats.stream()
                    .filter(s -> currentRoute.get().getAircraftCode().equalsIgnoreCase(s.getAircraftCode()))
                    .filter(s -> fareCondition.toString().equalsIgnoreCase(s.getFareConditions()))
                    .count();

            // Билеты, которые куплены по текущему перелету и совпадают по классу обслуживания
            long ticketFlightsCount = ticketFlights.stream()
                    .filter(tf -> flight.getFlightId().equals(tf.getFlightId()))
                    .count();


            if (ticketFlightsCount >= currentSeatsCount) {
                isMatched = false;
                break;
            }
        }

        return isMatched;
    }

    private void findRoutes(String currentAirport,
                            String arrivalAirport,
                            OffsetDateTime currentArrival,
                            List<FlightEntity> flights) {
        var currentFlights = flights.stream()
                .filter(f -> currentAirport.equalsIgnoreCase(f.getDepartureAirport()))
                .filter(f -> currentArrival.isBefore(f.getScheduledDeparture()) ||
                        currentArrival.isEqual(f.getScheduledDeparture()))
                .toList();

        var arrivalFlights = currentFlights.stream()
                .filter(f -> arrivalAirport.equalsIgnoreCase(f.getArrivalAirport()))
                .toList();

        for (var aFlight : arrivalFlights) {
            var route = (ArrayList<FlightEntity>) currentRoute.clone();
            route.add(aFlight);
            foundRoutes.add(route);
        }

        var nonArrivalFlights = currentFlights.stream()
                .filter(f -> !arrivalAirport.equalsIgnoreCase(f.getArrivalAirport()))
                .toList();

        for (var aFlight : nonArrivalFlights) {
            // Если возник цикл, то мы его пропускаем
            if (currentRoutesAirports.contains(aFlight.getArrivalAirport()))
                continue;

            currentRoutesAirports.add(aFlight.getArrivalAirport());
            currentRoute.add(aFlight);

            findRoutes(aFlight.getArrivalAirport(), arrivalAirport, aFlight.getScheduledArrival(), flights);

            currentRoute.remove(aFlight);
        }
    }

    private void clearRoutesState() {
        currentRoutesAirports.clear();

        if (!currentRoute.isEmpty())
            throw new IllegalStateException("ERROR current route is not empty");

        foundRoutes.clear();
    }
}
