package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.FlightEntity;
import edu.javadb.flightsspring.repos.FlightsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/flights")
public class FlightsController {
    private final FlightsRepository flightsRepository;

    @GetMapping
    public Page<FlightEntity> flights(Pageable pageable) {
        return flightsRepository.findAll(pageable);
    }
}
