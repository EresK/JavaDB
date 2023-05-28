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
@Table(name = "seats")
public class SeatEntity {
    @JsonIgnore
    @EmbeddedId
    private SeatKey seatKey;

    @Column(name = "fare_conditions")
    private String fareConditions;

    @JsonGetter("aircraftCode")
    public String getAircraftCode() {
        return seatKey.getAircraftCode();
    }

    @JsonGetter("seatNo")
    public String getSeatNo() {
        return seatKey.getSeatNo();
    }
}
