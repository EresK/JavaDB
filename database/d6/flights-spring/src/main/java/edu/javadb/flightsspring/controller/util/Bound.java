package edu.javadb.flightsspring.controller.util;

public enum Bound {
    INBOUND("INBOUND"),
    OUTBOUND("OUTBOUND");

    private final String bound;

    Bound(String bound) {
        this.bound = bound;
    }

    public static Bound build(String bound) {
        return switch (bound.toUpperCase()) {
            case "INBOUND" -> Bound.INBOUND;
            case "OUTBOUND" -> Bound.OUTBOUND;
            default -> throw new IllegalArgumentException("Only INBOUND and OUTBOUND allow for schedule");
        };
    }

    @Override
    public String toString() {
        return bound;
    }
}
