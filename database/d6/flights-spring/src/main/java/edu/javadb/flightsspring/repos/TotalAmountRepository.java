package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.TotalAmountEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TotalAmountRepository extends PagingAndSortingRepository<TotalAmountEntity, String> {
}
