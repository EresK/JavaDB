package edu.javadb.flightsspring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "grouped_amount")
public class GroupedAmountEntity {
    @JsonIgnore
    @EmbeddedId
    private GroupedAmountKey groupedAmountKey;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "seat_count")
    private Long seatCount;

    public String getFlightNo() {
        return groupedAmountKey.getFlightNo();
    }

    public String getFareConditions() {
        return groupedAmountKey.getFareConditions();
    }
}
