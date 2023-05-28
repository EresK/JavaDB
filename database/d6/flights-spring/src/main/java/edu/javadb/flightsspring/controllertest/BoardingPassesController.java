package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.BoardingPassEntity;
import edu.javadb.flightsspring.repos.BoardingPassesRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/boarding_passes")
public class BoardingPassesController {
    private final BoardingPassesRepository boardingPassesRepository;

    @GetMapping
    public Page<BoardingPassEntity> boardingPasses(Pageable pageable) {
        return boardingPassesRepository.findAll(pageable);
    }
}
