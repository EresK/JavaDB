package edu.javadb.flightsspring.repos;

import edu.javadb.flightsspring.domain.GroupedAmountEntity;
import edu.javadb.flightsspring.domain.GroupedAmountKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupedAmountRepository extends JpaRepository<GroupedAmountEntity, GroupedAmountKey> {
    List<GroupedAmountEntity> findAllByGroupedAmountKeyFlightNoIn(List<String> flightNos);
}
