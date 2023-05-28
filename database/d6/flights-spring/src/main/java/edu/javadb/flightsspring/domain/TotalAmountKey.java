package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
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
public class TotalAmountKey implements Serializable {
    @Column(name = "flight_no", length = 6)
    private String flightNo;

    @Column(name = "seat_no")
    private String seatNo;
}
