package edu.javadb.flightsspring.domain;

import edu.javadb.flightsspring.service.util.LocaleJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "aircrafts_data")
public class AircraftEntity {
    @Id
    @Column(name = "aircraft_code", length = 3, columnDefinition = "bpchar")
    private String aircraftCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "model")
    private LocaleJson model;

    @Column(name = "range")
    private Integer range;
}
