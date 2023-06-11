package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketsRepository extends JpaRepository<TicketEntity, String> {
}
