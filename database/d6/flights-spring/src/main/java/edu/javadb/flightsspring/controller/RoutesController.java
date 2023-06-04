package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.controller.response.FlightResponse;
import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import edu.javadb.flightsspring.service.RoutesService;
import edu.javadb.flightsspring.service.util.LocaleJson;
import edu.javadb.flightsspring.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class RoutesController {
    private final AirportsRepository airportsRepository;
    private final RoutesService routesService;

    @GetMapping("/cities/all")
    public List<String> cities(@RequestParam(name = "locale", required = false) String locale) {
        Locale localeEnum = Locale.getLocaleOrDefault(locale);

        var airports = airportsRepository.findAll();

        return switch (localeEnum) {
            case RU -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getRu).toList();
            case EN -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getEn).toList();
        };
    }

    @GetMapping("/routes")
    public Set<List<FlightResponse>> routes(@RequestParam(name = "departure") String departureAirport,
                                          @RequestParam(name = "arrival") String arrivalAirport,
                                          @RequestParam(name = "date") String date,
                                            @RequestParam(name = "booking_class", required = false) String fareConditions,
                                            @RequestParam(name = "additional_routes", required = false) Integer additionalRoutes) {
        OffsetDateTime dateTime = OffsetDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV"));

        if (additionalRoutes != null && additionalRoutes < 0)
            throw new IllegalArgumentException("If additional_routes param is present it can not be less than 0");

        var foundRoutes = routesService.findAllRoutes(departureAirport, arrivalAirport, dateTime, additionalRoutes);

        Set<List<FlightResponse>> responseRoutes = new HashSet<>();

        for (var list: foundRoutes)
            responseRoutes.add(list.stream().map(FlightResponse::fromEntity).toList());

        return responseRoutes;
    }
}
