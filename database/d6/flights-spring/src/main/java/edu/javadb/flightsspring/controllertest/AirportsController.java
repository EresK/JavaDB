package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.repos.AirportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
//@RestController
//@RequestMapping("/airports")
public class AirportsController {
    private final AirportsRepository airportsRepository;

//    @GetMapping
    public Page<AirportEntity> airports(Pageable pageable) {
        return airportsRepository.findAll(pageable);
    }
}
