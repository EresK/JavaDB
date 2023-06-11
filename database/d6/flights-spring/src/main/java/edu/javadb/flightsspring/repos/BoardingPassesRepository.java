package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.BoardingPassEntity;
import edu.javadb.flightsspring.domain.BoardingPassKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardingPassesRepository extends JpaRepository<BoardingPassEntity, BoardingPassKey> {
}
