package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TicketFlightEntity;
import edu.javadb.flightsspring.domain.TicketFlightKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketFlightsRepository extends JpaRepository<TicketFlightEntity, TicketFlightKey> {
    Page<TicketFlightEntity> findAllBy(Pageable pageable);

    List<TicketFlightEntity> findAllByFareConditions(String fareConditions);

    List<TicketFlightEntity> findAllByTicketFlightKeyFlightIdIn(List<Integer> flightIds);
}
