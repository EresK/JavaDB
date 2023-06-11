package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TotalAmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalAmountRepository extends JpaRepository<TotalAmountEntity, String> {
}
