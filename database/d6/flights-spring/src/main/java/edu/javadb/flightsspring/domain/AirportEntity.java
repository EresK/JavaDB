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
@Table(name = "airports")
public class AirportEntity {
    @Id
    @Column(name = "airport_code", length = 3)
    private String airportCode;

    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "city")
    private String city;

    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "timezone")
    private String timezone;
}
