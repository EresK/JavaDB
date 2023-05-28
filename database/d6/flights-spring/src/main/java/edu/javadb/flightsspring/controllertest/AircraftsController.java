package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.AircraftEntity;
import edu.javadb.flightsspring.repos.AircraftsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/aircrafts")
public class AircraftsController {
    private final AircraftsRepository aircraftsRepository;

    @GetMapping
    public Page<AircraftEntity> aircrafts(Pageable pageable) {
        return aircraftsRepository.findAll(pageable);
    }
}
