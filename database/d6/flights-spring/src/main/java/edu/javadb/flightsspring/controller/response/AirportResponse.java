package edu.javadb.flightsspring.controller.response;

import edu.javadb.flightsspring.domain.AirportEntity;
import edu.javadb.flightsspring.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AirportResponse {
    private String airportCode;
    private String airportName;
    private String city;
    private String coordinates;
    private String timezone;

    public static AirportResponse fromEntity(AirportEntity entity, Locale locale) {
        AirportResponse response = new AirportResponse();
        response.setAirportCode(entity.getAirportCode());
        switch (locale) {
            case RU -> {
                response.setAirportName(entity.getAirportName().getRu());
                response.setCity(entity.getCity().getRu());
            }
            case EN -> {
                response.setAirportName(entity.getAirportName().getEn());
                response.setCity(entity.getCity().getEn());
            }
        }
        response.setCoordinates(entity.getCoordinates());
        response.setTimezone(entity.getTimezone());

        return response;
    }
}
