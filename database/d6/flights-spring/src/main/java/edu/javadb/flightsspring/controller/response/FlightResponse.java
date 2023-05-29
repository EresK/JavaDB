package edu.javadb.flightsspring.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightResponse {
    private String flightNo;
    private String departureAirport;
    private String departureAirportName;
    private String arrivalAirport;
    private String arrivalAirportName;
    private OffsetDateTime scheduledDeparture;
    private OffsetDateTime scheduledArrival;
    private String[] daysOfWeek;
}
