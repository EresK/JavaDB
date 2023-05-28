package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.BoardingPassEntity;
import edu.javadb.flightsspring.domain.BoardingPassKey;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BoardingPassesRepository extends PagingAndSortingRepository<BoardingPassEntity, BoardingPassKey> {
}
