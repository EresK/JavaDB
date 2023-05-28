package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.SeatEntity;
import edu.javadb.flightsspring.repos.SeatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/seats")
public class SeatsController {
    private final SeatsRepository seatsRepository;

    @GetMapping
    public Page<SeatEntity> seats(Pageable pageable) {
        return seatsRepository.findAll(pageable);
    }
}
