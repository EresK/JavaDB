package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.TicketFlightEntity;
import edu.javadb.flightsspring.repos.TicketFlightsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/ticket_flights")
public class TicketFlightsController {
    private final TicketFlightsRepository ticketFlightsRepository;

    @GetMapping
    public Page<TicketFlightEntity> ticketFlights(Pageable pageable) {
        return ticketFlightsRepository.findAll(pageable);
    }
}
