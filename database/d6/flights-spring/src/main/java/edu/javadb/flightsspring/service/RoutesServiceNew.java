package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.service.util.FareConditions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoutesServiceNew {
    private final FlightsRepository flightsRepository;

    @Transactional
    public Set<List<FlightEntity>> findRoutes(String departureAirport,
                                              String arrivalAirport,
                                              OffsetDateTime departureDate,
                                              FareConditions fareCondition,
                                              int maxConnections) {
        if (maxConnections < 1)
            throw new IllegalArgumentException("Max connections must be positive number");

        var flights = flightsRepository.findAllByStatusAndScheduledDepartureBetween("Scheduled", departureDate, departureDate.plusMonths(1));

        System.out.println("Flights size " + flights.size());

        Set<List<FlightEntity>> foundRoutes = new HashSet<>();

        dfs(departureAirport, arrivalAirport,
                departureDate, null,
                maxConnections,
                flights, new ArrayList<>(),
                new HashSet<>(), foundRoutes);

        return foundRoutes;
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
