package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TicketEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketsRepository extends PagingAndSortingRepository<TicketEntity, String> {
}
