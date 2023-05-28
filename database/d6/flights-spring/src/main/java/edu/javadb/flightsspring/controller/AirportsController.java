package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.controller.util.BoundEnum;
import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.domain.FlightEntity;
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

import java.util.List;

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
    public List<FlightEntity> airportsSchedule(Pageable pageable,
                                               @RequestParam(name = "bound") String bound,
                                               @RequestParam(name = "airport_code") String airportCode) {
        BoundEnum boundEnum = BoundEnum.build(bound);
        String statusScheduled = "Scheduled";

        return switch (boundEnum) {
            case INBOUND -> flightsRepository.findAllByArrivalAirportAndStatus(airportCode, statusScheduled, pageable);
            case OUTBOUND ->
                    flightsRepository.findAllByDepartureAirportAndStatus(airportCode, statusScheduled, pageable);
        };
    }
}
