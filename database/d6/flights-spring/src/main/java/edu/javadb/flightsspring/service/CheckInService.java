package edu.javadb.flightsspring.service;

import edu.javadb.flightsspring.domain.*;
import edu.javadb.flightsspring.repos.BoardingPassesRepository;
import edu.javadb.flightsspring.repos.FlightsRepository;
import edu.javadb.flightsspring.repos.SeatsRepository;
import edu.javadb.flightsspring.repos.TicketFlightsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CheckInService {
    private TicketFlightsRepository ticketFlightsRepository;
    private BoardingPassesRepository boardingPassesRepository;
    private SeatsRepository seatsRepository;
    private FlightsRepository flightsRepository;

    @Transactional
    public BoardingPassEntity checkIn(int flightId, String ticketNo) throws Exception {
        var tf = getTicketFlight(flightId, ticketNo);

        if (hasBoardingPass(flightId, ticketNo))
            throw new Exception(String.format("Boarding pass is already exists id %d, no %s", flightId, ticketNo));

        var flight = flightsRepository.findById(Integer.valueOf(flightId).longValue());

        if (!(flight.isPresent() && flight.get().getStatus().equalsIgnoreCase("Scheduled")))
            throw new IllegalStateException("No flight with id: " + flightId);

        /* Check for seats */
        var listOfAllSeats = seatsRepository.findAllBySeatKeyAircraftCode(flight.get().getAircraftCode());
        var listOfBoardingPasses = boardingPassesRepository.findAllByBoardingPassKeyFlightId(flightId);
        var listOfIssuedSeats = listOfBoardingPasses.stream().map(BoardingPassEntity::getSeatNo).toList();

        if (listOfIssuedSeats.size() == listOfAllSeats.size())
            throw new Exception("There is no available seats for flight: " + flightId);

        listOfAllSeats.removeIf(s -> listOfIssuedSeats.contains(s.getSeatNo()));

        Optional<SeatEntity> seat = listOfAllSeats
                .stream()
                .filter(s -> s.getFareConditions().equalsIgnoreCase(tf.getFareConditions()))
                .findFirst();

        if (seat.isEmpty())
            throw new IllegalStateException("No free seats for booked ticket flight");

        /* Saving boarding pass */

        BoardingPassEntity boardingPass = new BoardingPassEntity();
        boardingPass.setBoardingPassKey(new BoardingPassKey(ticketNo, flightId));
        boardingPass.setSeatNo(seat.get().getSeatNo());
        boardingPass.setBoardingNo(getMaxBoardingNo(listOfBoardingPasses));

        boardingPassesRepository.save(boardingPass);

        return boardingPass;
    }

    private TicketFlightEntity getTicketFlight(int flightId, String ticketNo) throws Exception {
        var key = new TicketFlightKey(ticketNo, flightId);
        var tf = ticketFlightsRepository.findById(key);

        if (tf.isEmpty())
            throw new Exception(String.format("There is no ticket flight with id %d, no %s", flightId, ticketNo));

        return tf.get();
    }

    private boolean hasBoardingPass(int flightId, String ticketNo) {
        var key = new BoardingPassKey(ticketNo, flightId);
        var bp = boardingPassesRepository.findById(key);

        return bp.isPresent();
    }

    private int getMaxBoardingNo(List<BoardingPassEntity> boardingPasses) {
        int maxBoardingNo = 1;

        for (var b : boardingPasses) {
            if (maxBoardingNo <= b.getBoardingNo())
                maxBoardingNo = b.getBoardingNo() + 1;
        }

        return maxBoardingNo;
    }
}
