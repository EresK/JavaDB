package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.SeatEntity;
import edu.javadb.flightsspring.domain.SeatKey;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SeatsRepository extends PagingAndSortingRepository<SeatEntity, SeatKey> {
}
