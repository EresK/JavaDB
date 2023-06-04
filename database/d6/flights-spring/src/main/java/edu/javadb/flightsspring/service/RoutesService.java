package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.RoutesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoutesService {
    private final RoutesRepository routesRepository;
    private final FlightsRepository flightsRepository;

    private Set<String> currentRoutesAirports;
    private ArrayList<FlightEntity> currentRoute;
    private Set<List<FlightEntity>> foundRoutes;

    @Transactional
    public Set<List<FlightEntity>> findAllRoutes(String departureAirport,
                                                 String arrivalAirport,
                                                 OffsetDateTime departureDate,
                                                 Integer additionalRoutes) {
        List<FlightEntity> flights = flightsRepository.findAllByStatus("Scheduled");

        flights = flights.stream()
                .filter(f -> departureDate.isEqual(f.getScheduledDeparture()) ||
                        departureDate.isBefore(f.getScheduledDeparture()))
                .toList();

        findRoutes(departureAirport, arrivalAirport, departureDate, flights);

        if (additionalRoutes != null)
            foundRoutes.removeIf(list -> list.size() > (additionalRoutes + 1));

        var currentFoundRoutes = Set.copyOf(foundRoutes);

        clearRoutesState();

        return currentFoundRoutes;
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
            throw new RuntimeException("ERROR current route is not empty");

        foundRoutes.clear();
    }
}
