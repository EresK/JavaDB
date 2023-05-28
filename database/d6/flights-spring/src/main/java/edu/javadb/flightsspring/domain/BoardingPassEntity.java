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

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "boarding_passes")
public class BoardingPassEntity {
    @JsonIgnore
    @EmbeddedId
    private BoardingPassKey boardingPassKey;

    @Column(name = "boarding_no")
    private Integer boardingNo;

    @Column(name = "seat_no")
    private String seatNo;

    @JsonGetter("ticketNo")
    public String getTicketNo() {
        return boardingPassKey.getTicketNo();
    }

    @JsonGetter("flightId")
    public Integer getFlightId() {
        return boardingPassKey.getFlightId();
    }
}
