package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GroupedAmountKey implements Serializable {
    @Column(name = "flight_no")
    private String flightNo;

    @Column(name = "fare_conditions")
    private String fareConditions;
}
