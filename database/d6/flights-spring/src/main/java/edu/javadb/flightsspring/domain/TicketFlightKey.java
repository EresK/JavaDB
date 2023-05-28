package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TicketFlightKey implements Serializable {
    @Column(name = "ticket_no", length = 13)
    private String ticketNo;

    @Column(name = "flight_id")
    private Integer flightId;
}
