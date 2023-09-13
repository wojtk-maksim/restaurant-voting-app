package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Lunch;

import java.time.LocalDate;

import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantAvailable;
import static ru.javaops.restaurantvoting.util.RestaurantUtil.checkRestaurantDeleted;

public class LunchUtil {
    public static final String LUNCH = "Lunch";

    public static void checkLunchExists(Lunch lunch, Long restaurantId, LocalDate date) {
        if (lunch == null) {
            throw new NotFoundException(LUNCH + " on date " + date + " in restaurant " + restaurantId);
        }
    }

    public static void checkLunchAvailableForVoting(Lunch lunch, Long restaurantId, LocalDate date) {
        if (!lunch.isEnabled()) {
            throw new RuntimeException(LUNCH + " from restaurant " + restaurantId + " on " + date + " was withdrawn");
        }
        checkRestaurantAvailable(lunch.getRestaurant());
        checkRestaurantDeleted(lunch.getRestaurant());
    }

}
