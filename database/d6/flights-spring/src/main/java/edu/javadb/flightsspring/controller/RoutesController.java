package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class RoutesController {
    private final AirportsRepository airportsRepository;

    @GetMapping("/cities/all")
    public List<String> cities() {
        var airports = airportsRepository.findAll();
        return airports.stream().map(AirportEntity::getCity).distinct().toList();
    }
}
