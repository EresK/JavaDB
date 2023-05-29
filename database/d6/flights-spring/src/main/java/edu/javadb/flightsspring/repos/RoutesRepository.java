package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.RouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutesRepository extends JpaRepository<RouteEntity, String> {
    List<RouteEntity> findAllByArrivalAirport(String arrivalAirport);

    List<RouteEntity> findAllByDepartureAirport(String departureAirport);

    Page<RouteEntity> findAll(Pageable pageable);
}
