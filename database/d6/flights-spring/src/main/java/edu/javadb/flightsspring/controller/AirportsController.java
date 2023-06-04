package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.controller.response.*;
import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.domain.RouteEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.RoutesRepository;
import edu.javadb.flightsspring.util.DaysOfWeek;
import edu.javadb.flightsspring.util.Locale;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/airports")
public class AirportsController {
    private final AirportsRepository airportsRepository;
    private final RoutesRepository routesRepository;
    private final FlightsRepository flightsRepository;

    @GetMapping("/all")
    public List<AirportResponse> airports(Pageable pageable,
                                          @RequestParam(name = "locale", required = false) String locale) {
        Locale localeEnum = Locale.getLocaleOrDefault(locale);

        return airportsRepository.findAll(pageable).stream()
                .map(anEntity -> AirportResponse.fromEntity(anEntity, localeEnum))
                .toList();
    }

    @GetMapping(params = {"!bound", "!airport_code"})
    public List<AirportResponse> airportsWithinACity(@RequestParam(name = "city") String city,
                                                     @RequestParam(name = "locale", required = false) String locale) {
        Locale localeEnum = Locale.getLocaleOrDefault(locale);

        var airports = airportsRepository.findAll();

        return switch (localeEnum) {
            case RU -> airports.stream()
                    .filter(a -> city.equalsIgnoreCase(a.getCity().getRu()))
                    .map(anEntity -> AirportResponse.fromEntity(anEntity, localeEnum))
                    .toList();
            case EN -> airports.stream()
                    .filter(a -> city.equalsIgnoreCase(a.getCity().getEn()))
                    .map(anEntity -> AirportResponse.fromEntity(anEntity, localeEnum))
                    .toList();
        };
    }

    @GetMapping(params = {"!city"})
    @Transactional
    public List<FlightResponse> airportsSchedule(Pageable pageable,
                                                 @RequestParam(name = "bound") String bound,
                                                 @RequestParam(name = "airport_code") String airportCode,
                                                 @RequestParam(name = "locale", required = false) String locale) {
        Bound boundEnum = Bound.build(bound);
        DaysOfWeek daysOfWeek = new DaysOfWeek(Locale.getLocaleOrDefault(locale));
        final String statusScheduled = "Scheduled";

        List<RouteEntity> routes = null;
        List<FlightEntity> flights = null;

        switch (boundEnum) {
            case INBOUND -> {
                routes = routesRepository.findAllByArrivalAirport(airportCode);
                flights = flightsRepository.findAllByArrivalAirportAndStatus(airportCode, statusScheduled, pageable);
            }
            case OUTBOUND -> {
                routes = routesRepository.findAllByDepartureAirport(airportCode);
                flights = flightsRepository.findAllByDepartureAirportAndStatus(airportCode, statusScheduled, pageable);
            }
        }

        var flightsResponse = new ArrayList<FlightResponse>();

        for (var f : flights) {
            Optional<RouteEntity> fRoute = routes.stream()
                    .filter(r -> f.getFlightNo().equalsIgnoreCase(r.getFlightNo()))
                    .findFirst();

            if (fRoute.isEmpty())
                throw new IllegalStateException("No route is found for flight no: " + f.getFlightNo());

            var days = Arrays.stream(fRoute.get().getDaysOfWeek()).mapToObj(daysOfWeek::getDay).toList().toArray(new String[]{});

            FlightResponse response = switch (boundEnum) {
                case INBOUND -> new InboundFlightResponse(
                        f.getDepartureAirport(),
                        f.getDepartureAirportName(),
                        f.getDepartureCity(),
                        f.getScheduledArrival(),
                        f.getFlightNo(),
                        days);
                case OUTBOUND -> new OutboundFlightResponse(
                        f.getArrivalAirport(),
                        f.getArrivalAirportName(),
                        f.getArrivalCity(),
                        f.getScheduledDeparture(),
                        f.getFlightNo(),
                        days);
            };

            flightsResponse.add(response);
        }

        return flightsResponse;
    }
}
