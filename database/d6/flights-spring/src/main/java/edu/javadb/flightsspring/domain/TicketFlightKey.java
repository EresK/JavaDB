package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Embeddable
public class TicketFlightKey implements Serializable {
    @Column(name = "ticket_no", length = 13)
    private String ticketNo;

    @Column(name = "flight_id")
    private Integer flightId;
}
