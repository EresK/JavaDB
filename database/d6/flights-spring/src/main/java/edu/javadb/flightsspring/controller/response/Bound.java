package edu.javadb.flightsspring.controller.response;

public enum Bound {
    INBOUND,
    OUTBOUND;

    public static Bound build(String bound) {
        return switch (bound.toUpperCase()) {
            case "INBOUND" -> Bound.INBOUND;
            case "OUTBOUND" -> Bound.OUTBOUND;
            default -> throw new IllegalArgumentException("Only INBOUND and OUTBOUND bounds allow for schedule");
        };
    }
}
