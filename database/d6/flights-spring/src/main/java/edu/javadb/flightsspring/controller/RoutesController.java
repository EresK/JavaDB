package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import edu.javadb.flightsspring.service.util.LocaleJson;
import edu.javadb.flightsspring.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class RoutesController {
    private final AirportsRepository airportsRepository;

    @GetMapping("/cities/all")
    public List<String> cities(@RequestParam(name = "locale", required = false) String locale) {
        Locale localeEnum = Locale.getLocaleOrDefault(locale);

        var airports = airportsRepository.findAll();

        return switch (localeEnum) {
            case RU -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getRu).toList();
            case EN -> airports.stream().map(AirportEntity::getCity).map(LocaleJson::getEn).toList();
        };
    }
}
