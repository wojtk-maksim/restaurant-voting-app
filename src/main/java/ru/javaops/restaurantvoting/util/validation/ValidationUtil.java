package ru.javaops.restaurantvoting.util.validation;

import ru.javaops.restaurantvoting.error.DeadLineException;
import ru.javaops.restaurantvoting.error.DeletedEntityException;
import ru.javaops.restaurantvoting.error.NotAvailableException;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Deletable;
import ru.javaops.restaurantvoting.model.Enablable;

import java.time.*;

public class ValidationUtil {

    private static final LocalTime DEADLINE = LocalTime.of(11, 0);

    private static final ZoneId TIMEZONE = ZoneId.of("Europe/Moscow");

    public static void checkDeadline(LocalDate date, String action) {
        LocalDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), TIMEZONE).toLocalDateTime();
        LocalDateTime deadline = LocalDateTime.of(date, DEADLINE);
        if (currentDateTime.isAfter(deadline)) {
            throw new DeadLineException(action);
        }
    }

    public static <T> T checkExists(T obj, String objectClass, Object id) {
        if (obj == null) {
            throw new NotFoundException(objectClass + " " + id);
        }
        return obj;
    }

    public static void checkDeleted(Deletable obj, String objectClass, Long id) {
        if (obj.isDeleted()) {
            throw new DeletedEntityException(objectClass + " " + id);
        }
    }

    public static void checkDeletedInRestaurant(Deletable obj, String objectClass, Long restaurantId, Long id) {
        if (obj.isDeleted()) {
            throw new DeletedEntityException(objectClass + " " + id);
        }
    }

    public static <T> T checkExistsInRestaurant(T obj, String objectClass, Long restaurantId, Long id) {
        if (obj == null) {
            throw new NotFoundException(objectClass + " " + id + " in restaurant " + restaurantId);
        }
        return obj;
    }

    public static void checkAvailable(Enablable obj, String objectClass, Long id) {
        if (!obj.isEnabled()) {
            throw new NotAvailableException(objectClass + " " + id);
        }
    }

    public static void checkAvailableInRestaurant(Enablable obj, String objectClass, Long id, Long restaurantId) {
        if (!obj.isEnabled()) {
            throw new NotAvailableException(objectClass + " " + id + " in restaurant " + restaurantId);
        }
    }

}
