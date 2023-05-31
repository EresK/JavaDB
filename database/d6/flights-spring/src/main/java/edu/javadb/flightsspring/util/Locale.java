package edu.javadb.flightsspring.util;

public enum Locale {
    RU,
    EN;

    public static Locale getLocale(String locale) {
        return switch (locale.toUpperCase()) {
            case "RU", "RUSSIAN" -> RU;
            case "EN", "ENGLISH" -> EN;
            default -> throw new IllegalArgumentException("Only RU and EN locales are available");
        };
    }

    public static Locale getLocaleOrDefault(String locale) {
        if (locale == null)
            return RU;

        return switch (locale.toUpperCase()) {
            case "EN", "ENGLISH" -> EN;
            default -> RU;
        };
    }
}
