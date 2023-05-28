package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.AircraftEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AircraftsRepository extends PagingAndSortingRepository<AircraftEntity, String> {
}
