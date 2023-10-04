package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.Enablable;
import ru.javaops.restaurantvoting.HasName;
import ru.javaops.restaurantvoting.error.NotAvailableException;

import java.time.LocalDate;

public class LunchUtil extends ValidationUtil {

    public static final String LUNCH_CODE = "lunch";

    public static void checkLunchFound(Object lunch, LocalDate date, HasName restaurant) {
        checkFoundInRestaurant("lunch", lunch, date, restaurant);
    }

    public static void checkLunchAvailable(Enablable lunch, LocalDate date, HasName restaurant) {
        if (!lunch.isEnabled()) {
            throw new NotAvailableException(messageSourceAccessor.getMessage(
                    "error.notAvailable." + LUNCH_CODE,
                    new String[]{date.toString(), restaurant.getName()}
            ));
        }
    }

}
