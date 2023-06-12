package edu.javadb.flightsspring.controller.response;

import edu.javadb.flightsspring.domain.FlightEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FlightResponse {
    private int flightId;
    private String flightNo;

    private OffsetDateTime scheduledDeparture;
    private OffsetDateTime scheduledArrival;

    private LocalDateTime scheduledDepartureLocal;
    private LocalDateTime scheduledArrivalLocal;

    private String departureAirport;
    private String departureAirportName;
    private String departureCity;

    private String arrivalAirport;
    private String arrivalAirportName;
    private String arrivalCity;

    private String aircraftCode;

    private String fareConditions;
    private BigDecimal amount;

    public static FlightResponse fromEntity(FlightEntity entity, String fareConditions, BigDecimal amount) {
        FlightResponse response = new FlightResponse();

        response.setFlightId(entity.getFlightId());
        response.setFlightNo(entity.getFlightNo());

        response.setScheduledDeparture(entity.getScheduledDeparture());
        response.setScheduledArrival(entity.getScheduledArrival());

        response.setScheduledDepartureLocal(entity.getScheduledDepartureLocal());
        response.setScheduledArrivalLocal(entity.getScheduledArrivalLocal());

        response.setDepartureAirport(entity.getDepartureAirport());
        response.setDepartureAirportName(entity.getDepartureAirportName());
        response.setDepartureCity(entity.getDepartureCity());

        response.setArrivalAirport(entity.getArrivalAirport());
        response.setArrivalAirportName(entity.getArrivalAirportName());
        response.setArrivalCity(entity.getArrivalCity());

        response.setAircraftCode(entity.getAircraftCode());

        response.setFareConditions(fareConditions);
        response.setAmount(amount);

        return response;
    }
}
