package edu.javadb.flightsspring.service.util;

public enum FareConditions {
    ECONOMY("ECONOMY"),
    COMFORT("COMFORT"),
    BUSINESS("BUSINESS");

    private final String fareCondition;

    FareConditions(String fareCondition) {
        this.fareCondition = fareCondition;
    }

    public static FareConditions getFareCondition(String fareCondition) {
        if (fareCondition == null)
            throw new NullPointerException("Argument can not be null");

        return switch (fareCondition.toUpperCase()) {
            case "ECONOMY" -> ECONOMY;
            case "COMFORT" -> COMFORT;
            case "BUSINESS" -> BUSINESS;
            default -> throw new IllegalArgumentException("Fare condition must be Economy, Comfort or Business");
        };
    }

    @Override
    public String toString() {
        return fareCondition;
    }
}
