package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.AirportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportsRepository extends JpaRepository<AirportEntity, String> {
    Page<AirportEntity> findAll(Pageable pageable);
}
