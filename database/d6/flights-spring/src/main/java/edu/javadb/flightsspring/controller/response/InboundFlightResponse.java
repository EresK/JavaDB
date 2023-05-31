package edu.javadb.flightsspring.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class InboundFlightResponse extends FlightResponse {
    private String originAirport;
    private String originAirportName;
    private String originCity;
    private OffsetDateTime scheduledArrival;

    public InboundFlightResponse(String originAirport,
                                 String originAirportName,
                                 String originCity,
                                 OffsetDateTime scheduledArrival,
                                 String flightNo,
                                 String[] daysOfWeek) {
        super(flightNo, daysOfWeek);

        this.originAirport = originAirport;
        this.originAirportName = originAirportName;
        this.originCity = originCity;
        this.scheduledArrival = scheduledArrival;
    }
}
