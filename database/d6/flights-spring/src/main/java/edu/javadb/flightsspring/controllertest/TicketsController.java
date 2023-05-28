package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.TicketEntity;
import edu.javadb.flightsspring.repos.TicketsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketsController {
    private final TicketsRepository ticketsRepository;

    @GetMapping
    public Page<TicketEntity> tickets(Pageable pageable) {
        return ticketsRepository.findAll(pageable);
    }
}
