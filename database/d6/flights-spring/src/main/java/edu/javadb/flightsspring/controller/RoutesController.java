package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.controller.response.FlightResponse;
import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import edu.javadb.flightsspring.service.RoutesServiceNew;
import edu.javadb.flightsspring.service.util.FareConditions;
import edu.javadb.flightsspring.service.util.LocaleJson;
import edu.javadb.flightsspring.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class RoutesController {
    private final AirportsRepository airportsRepository;
    private final RoutesServiceNew routesServiceNew;

    @GetMapping("/cities/all")
    public List<String> cities(@RequestParam(name = "locale", required = false) String locale) {
        Locale localeEnum = Locale.getLocaleOrDefault(locale);

        var airports = airportsRepository.findAll();

        return switch (localeEnum) {
            case RU -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getRu).toList();
            case EN -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getEn).toList();
        };
    }

    @GetMapping("/routes/new")
    public Set<List<FlightResponse>> routesNew(@RequestParam(name = "departure") String departureAirport,
                                               @RequestParam(name = "arrival") String arrivalAirport,
                                               @RequestParam(name = "date") String date,
                                               @RequestParam(name = "fare_conditions") String fareConditions,
                                               @RequestParam(name = "max_connections", required = false) Integer maxConnections) {
        OffsetDateTime dateTime = OffsetDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV"));
        FareConditions fareConditionEnum = FareConditions.getFareCondition(fareConditions);

        maxConnections = (maxConnections == null || maxConnections < 1) ? 1 : maxConnections;
        maxConnections = (maxConnections > 3) ? 3 : maxConnections;

        return routesServiceNew.findRoutes(departureAirport, arrivalAirport, dateTime, fareConditionEnum, maxConnections);
    }
}
