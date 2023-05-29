package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.controller.response.FlightResponse;
import edu.javadb.flightsspring.controller.util.Bound;
import edu.javadb.flightsspring.controller.util.DaysOfWeek;
import edu.javadb.flightsspring.controller.util.Locale;
import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.domain.RouteEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.RoutesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<AirportEntity> airports(Pageable pageable) {
        return airportsRepository.findAll(pageable);
    }

    @GetMapping(params = {"!bound", "!airport_code"})
    public List<AirportEntity> airportsWithinACity(@RequestParam(name = "city") String city) {
        var airports = airportsRepository.findAll();
        return airports.stream().filter(airportEntity -> city.equalsIgnoreCase(airportEntity.getCity())).toList();
    }

    @GetMapping(params = {"!city"})
    public List<FlightResponse> airportsSchedule(Pageable pageable,
                                                 @RequestParam(name = "bound") String bound,
                                                 @RequestParam(name = "airport_code") String airportCode) {
        Bound boundEnum = Bound.build(bound);
        DaysOfWeek daysOfWeek = new DaysOfWeek(Locale.RU);
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

            var days = Arrays.stream(fRoute.get().getDaysOfWeek()).mapToObj(daysOfWeek::numToDay).toList().toArray(new String[]{});

            FlightResponse response = new FlightResponse(
                    f.getFlightNo(),
                    f.getDepartureAirport(), f.getDepartureAirportName(),
                    f.getArrivalAirport(), f.getArrivalAirportName(),
                    f.getScheduledDeparture(), f.getScheduledArrival(),
                    days);

            flightsResponse.add(response);
        }

        return flightsResponse;
    }
}
