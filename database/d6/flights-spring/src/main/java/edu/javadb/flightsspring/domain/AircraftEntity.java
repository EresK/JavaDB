package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "aircrafts")
public class AircraftEntity {
    @Id
    @Column(name = "aircraft_code", length = 3)
    private String aircraftCode;

    @Column(name = "model")
    private String model;

    @Column(name = "range")
    private Integer range;
}
