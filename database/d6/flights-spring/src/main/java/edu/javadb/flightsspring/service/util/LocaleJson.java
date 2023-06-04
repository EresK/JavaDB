package edu.javadb.flightsspring.service.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LocaleJson implements Serializable {
    private String en;
    private String ru;
}
