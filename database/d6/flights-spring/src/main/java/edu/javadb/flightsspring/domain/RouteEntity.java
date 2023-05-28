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
@Table(name = "routes")
public class RouteEntity {
    @Id
    @Column(name = "flight_no", length = 6)
    private String flightNo;

    @Column(name = "departure_airport", length = 3)
    private String departureAirport;

    @Column(name = "departure_airport_name")
    private String departureAirportName;

    @Column(name = "departure_city")
    private String departureCity;

    @Column(name = "arrival_airport", length = 3)
    private String arrivalAirport;

    @Column(name = "arrival_airport_name")
    private String arrivalAirportName;

    @Column(name = "arrival_city")
    private String arrivalCity;

    @Column(name = "aircraft_code", length = 3)
    private String aircraftCode;

    @Column(name = "days_of_week")
    private int[] daysOfWeek;
}
