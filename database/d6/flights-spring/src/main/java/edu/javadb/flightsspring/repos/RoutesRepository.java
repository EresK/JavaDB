package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.RouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutesRepository extends JpaRepository<RouteEntity, String> {
    Page<RouteEntity> findAll(Pageable pageable);
}
