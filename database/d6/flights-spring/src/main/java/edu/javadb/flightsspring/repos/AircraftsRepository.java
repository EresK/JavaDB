package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftsRepository extends JpaRepository<AircraftEntity, String> {
}
