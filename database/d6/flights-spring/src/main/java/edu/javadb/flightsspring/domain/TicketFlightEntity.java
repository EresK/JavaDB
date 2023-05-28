package edu.javadb.flightsspring.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket_flights")
public class TicketFlightEntity {
    @JsonIgnore
    @EmbeddedId
    private TicketFlightKey ticketFlightKey;

    @Column(name = "fare_conditions")
    private String fareConditions;

    @Column(name = "amount")
    private BigDecimal amount;

    @JsonGetter("ticketNo")
    public String getTicketNo() {
        return ticketFlightKey.getTicketNo();
    }

    @JsonGetter("flightId")
    public Integer getFlightId() {
        return ticketFlightKey.getFlightId();
    }
}
