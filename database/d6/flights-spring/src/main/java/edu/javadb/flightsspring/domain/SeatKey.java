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
public class SeatKey implements Serializable {
    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Column(name = "seat_no")
    private String seatNo;
}
