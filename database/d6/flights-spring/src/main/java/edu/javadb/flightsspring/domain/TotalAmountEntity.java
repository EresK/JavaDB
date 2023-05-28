package edu.javadb.flightsspring.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
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
@Table(name = "total_amount")
public class TotalAmountEntity {
    @JsonIgnore
    @EmbeddedId
    private TotalAmountKey totalAmountKey;

    @Column(name = "fare_conditions")
    private String fareConditions;

    @Column(name = "amount")
    private BigDecimal amount;

    @JsonGetter("flightNo")
    public String getFlightNo() {
        return totalAmountKey.getFlightNo();
    }

    @JsonGetter("seatNo")
    public String getSeatNo() {
        return totalAmountKey.getSeatNo();
    }
}
