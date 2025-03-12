package pl.sgorski.EPlanner.utils;

import java.time.LocalDate;

public class DateUtils {
    public static LocalDate getLastDayOfMonth(LocalDate day) {
        return day.withDayOfMonth(day.getMonth().length(day.isLeapYear()));
    }
}
