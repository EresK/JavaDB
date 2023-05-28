package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.FlightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightsRepository extends JpaRepository<FlightEntity, Long> {
    List<FlightEntity> findAllByArrivalAirportAndStatus(String arrivalAirport, String status, Pageable pageable);

    List<FlightEntity> findAllByDepartureAirportAndStatus(String departureAirport, String status, Pageable pageable);

    Page<FlightEntity> findAll(Pageable pageable);
}
