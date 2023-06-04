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
@Table(name = "airports_data")
public class AirportEntity {
    @Id
    @Column(name = "airport_code", length = 3, columnDefinition = "bpchar")
    private String airportCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "airport_name")
    private LocaleJson airportName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "city")
    private LocaleJson city;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "coordinates", columnDefinition = "point")
    private String coordinates;

    @Column(name = "timezone", columnDefinition = "text", length = Integer.MAX_VALUE)
    private String timezone;
}
