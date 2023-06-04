package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.RoutesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RoutesService {
    private final RoutesRepository routesRepository;
    private final FlightsRepository flightsRepository;

    private Set<String> currentRoutesAirports = new HashSet<>();
    private ArrayList<FlightEntity> currentRoute = new ArrayList<>();
    private Set<List<FlightEntity>> foundRoutes = new HashSet<>();

    public void findAllRoutes(String departureAirport, String arrivalAirport, OffsetDateTime departureDate) {
        List<FlightEntity> flights = flightsRepository.findAllByStatus("Scheduled");

        flights = flights.stream()
                .filter(f ->
                        departureDate.isEqual(f.getScheduledDeparture()) ||
                                departureDate.isBefore(f.getScheduledDeparture()))
                .toList();

    }

    private void findRoutes(String currentAirport, String arrivalAirport, List<FlightEntity> flights) {
        var currentFlights = flights.stream()
                .filter(f -> currentAirport.equalsIgnoreCase(f.getDepartureAirport()))
                .toList();

        var arrivalFlights = currentFlights.stream()
                .filter(f -> arrivalAirport.equalsIgnoreCase(f.getArrivalAirport()))
                .toList();

        for (var aFlight: arrivalFlights) {
            var route = (ArrayList<FlightEntity>) currentRoute.clone();
            route.add(aFlight);
            foundRoutes.add(route);
        }

        var nonArrivalFlights = currentFlights.stream()
                .filter(f -> !arrivalAirport.equalsIgnoreCase(f.getArrivalAirport()))
                .toList();

        for (var aFlight: nonArrivalFlights) {
            
        }
    }
}
