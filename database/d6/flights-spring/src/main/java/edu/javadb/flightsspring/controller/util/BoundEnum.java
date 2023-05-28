package edu.javadb.flightsspring.controller.util;

public enum BoundEnum {
    INBOUND("INBOUND"),
    OUTBOUND("OUTBOUND");

    private final String bound;

    BoundEnum(String bound) {
        this.bound = bound;
    }

    public static BoundEnum build(String bound) {
        return switch (bound.toUpperCase()) {
            case "INBOUND" -> BoundEnum.INBOUND;
            case "OUTBOUND" -> BoundEnum.OUTBOUND;
            default -> throw new IllegalArgumentException("Only INBOUND and OUTBOUND allow for schedule");
        };
    }

    @Override
    public String toString() {
        return bound;
    }
}
