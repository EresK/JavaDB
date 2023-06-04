package edu.javadb.flightsspring.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractFlightResponse {
    private String flightNo;
    private String[] daysOfWeek;

    public AbstractFlightResponse(String flightNo, String[] daysOfWeek) {
        this.flightNo = flightNo;
        this.daysOfWeek = daysOfWeek;
    }
}
