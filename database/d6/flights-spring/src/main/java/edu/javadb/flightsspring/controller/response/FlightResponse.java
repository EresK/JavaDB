package edu.javadb.flightsspring.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FlightResponse {
    private String flightNo;
    private String[] daysOfWeek;

    public FlightResponse(String flightNo, String[] daysOfWeek) {
        this.flightNo = flightNo;
        this.daysOfWeek = daysOfWeek;
    }
}
