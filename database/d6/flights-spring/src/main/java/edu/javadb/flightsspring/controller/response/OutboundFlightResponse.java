package edu.javadb.flightsspring.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class OutboundFlightResponse extends AbstractFlightResponse {
    private String destinationAirport;
    private String destinationAirportName;
    private String destinationCity;
    private OffsetDateTime scheduledDeparture;

    public OutboundFlightResponse(String destinationAirport,
                                 String destinationAirportName,
                                 String destinationCity,
                                 OffsetDateTime scheduledDeparture,
                                 String flightNo,
                                 String[] daysOfWeek) {
        super(flightNo, daysOfWeek);

        this.destinationAirport = destinationAirport;
        this.destinationAirportName = destinationAirportName;
        this.destinationCity = destinationCity;
        this.scheduledDeparture = scheduledDeparture;
    }
}
