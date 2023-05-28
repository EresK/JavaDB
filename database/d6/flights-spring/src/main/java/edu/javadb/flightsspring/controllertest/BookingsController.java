package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.BookingEntity;
import edu.javadb.flightsspring.repos.BookingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingsController {
    private final BookingsRepository bookingsRepository;

    @GetMapping
    public Page<BookingEntity> bookings(Pageable pageable) {
        return bookingsRepository.findAll(pageable);
    }
}
