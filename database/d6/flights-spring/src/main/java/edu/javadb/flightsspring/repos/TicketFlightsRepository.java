package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TicketFlightEntity;
import edu.javadb.flightsspring.domain.TicketFlightKey;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketFlightsRepository extends PagingAndSortingRepository<TicketFlightEntity, TicketFlightKey> {
}
