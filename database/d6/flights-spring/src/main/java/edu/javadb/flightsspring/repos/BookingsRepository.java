package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookingsRepository extends JpaRepository<BookingEntity, String> {
}
