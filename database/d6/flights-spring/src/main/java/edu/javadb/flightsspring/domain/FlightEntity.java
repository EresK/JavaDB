package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "flights_v")
public class FlightEntity {
    @Id
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "flight_no", length = 6)
    private String flightNo;

    @Column(name = "scheduled_departure")
    private OffsetDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private OffsetDateTime scheduledArrival;

    @Column(name = "actual_departure")
    private OffsetDateTime actualDeparture;

    @Column(name = "actual_arrival")
    private OffsetDateTime actualArrival;

    @Column(name = "scheduled_departure_local")
    private LocalDateTime scheduledDepartureLocal;

    @Column(name = "scheduled_arrival_local")
    private LocalDateTime scheduledArrivalLocal;

    @Column(name = "actual_departure_local")
    private LocalDateTime actualDepartureLocal;

    @Column(name = "actual_arrival_local")
    private LocalDateTime actualArrivalLocal;

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

    @Column(name = "status")
    private String status;

    @Column(name = "aircraft_code", length = 3)
    private String aircraftCode;
}
