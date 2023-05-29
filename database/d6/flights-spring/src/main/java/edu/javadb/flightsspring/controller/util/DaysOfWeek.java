package edu.javadb.flightsspring.controller.util;

public class DaysOfWeek {
    private static final String[] DAYS_RU = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресение"};
    private static final String[] DAYS_EN = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private final Locale locale;

    public DaysOfWeek(Locale locale) {
        this.locale = locale;
    }

    public String numToDay(int num) {
        if (num < 1 || num > 7)
            throw new IllegalArgumentException("Only number from 1 to 7 is possible");

        return switch (locale) {
            case RU -> DAYS_RU[num - 1];
            case EN -> DAYS_EN[num - 1];
        };
    }
}
