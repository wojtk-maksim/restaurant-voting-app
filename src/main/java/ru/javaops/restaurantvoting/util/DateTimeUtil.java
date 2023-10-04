package ru.javaops.restaurantvoting.util;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.restaurantvoting.error.DeadLineException;

import java.time.*;

@Slf4j
public class DateTimeUtil extends ValidationUtil {

    private static LocalTime DEADLINE;

    private static ZoneId TIMEZONE;

    public static void setDeadlineData(String time, String timezone) {
        DEADLINE = LocalTime.parse(time);
        TIMEZONE = ZoneId.of(timezone);
    }

    public static void checkDeadline(LocalDate date, String code) {
        LocalDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), TIMEZONE).toLocalDateTime();
        LocalDateTime deadline = LocalDateTime.of(date, DEADLINE);
        log.debug("Check if deadline {} has expired", deadline);
        if (currentDateTime.isAfter(deadline)) {
            throw new DeadLineException(messageSourceAccessor.getMessage("error.deadline." + code));
        }
    }

}
