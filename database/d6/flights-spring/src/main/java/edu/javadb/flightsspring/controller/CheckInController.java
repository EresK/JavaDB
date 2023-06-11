package edu.javadb.flightsspring.controller;

import edu.javadb.flightsspring.domain.BoardingPassEntity;
import edu.javadb.flightsspring.service.CheckInService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("checkin")
public class CheckInController {
    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<BoardingPassEntity> checkIn(@RequestParam(name = "flight_id") int flightId,
                                                      @RequestParam(name = "ticket_no") String ticketNo) {
        ResponseEntity<BoardingPassEntity> response;

        try {
            var boardingPass = checkInService.checkIn(flightId, ticketNo);
            response = new ResponseEntity<>(boardingPass, HttpStatus.CREATED);
        } catch (Exception e) {
            response = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
