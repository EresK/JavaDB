package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TotalAmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TotalAmountRepository extends JpaRepository<TotalAmountEntity, String> {
}
