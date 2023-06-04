package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.SeatEntity;
import edu.javadb.flightsspring.domain.SeatKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatsRepository extends JpaRepository<SeatEntity, SeatKey> {
}
