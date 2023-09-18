package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.error.*;
import ru.javaops.restaurantvoting.model.*;

import java.time.*;

public class ValidationUtil {

    private static final LocalTime DEADLINE = LocalTime.of(11, 0);
    private static final ZoneId TIMEZONE = ZoneId.of("Europe/Moscow");

    public static final String USER = User.class.getSimpleName();
    public static final String RESTAURANT = Restaurant.class.getSimpleName();
    public static final String DISH = Dish.class.getSimpleName();
    public static final String LUNCH = Lunch.class.getSimpleName();

    public static void checkDeadline(LocalDate date, String action) {
        LocalDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), TIMEZONE).toLocalDateTime();
        LocalDateTime deadline = LocalDateTime.of(date, DEADLINE);
        if (currentDateTime.isAfter(deadline)) {
            throw new DeadLineException(action);
        }
    }

    public static void checkExists(Object object, String objectClass, Object identifier) {
        if (object == null) {
            throw new NotFoundException(objectClass + " " + identifier);
        }
    }

    public static void checkAvailable(Enablable object) {
        if (!object.isEnabled()) {
            throw new NotAvailableException(object.toString());
        }
    }

    public static void checkDeleted(Deletable object) {
        if (object.isDeleted()) {
            throw new DeletedEntityException(object.toString());
        }
    }

    public static void checkUserExists(User user, Object identifier) {
        checkExists(user, USER, identifier);
    }

    public static void checkUserBanned(User user) {
        if (!user.isEnabled()) {
            throw new BannedUserException(user);
        }
    }

    public static void checkRestaurantExists(Restaurant restaurant, Object identifier) {
        checkExists(restaurant, RESTAURANT, identifier);
    }

    public static void checkDishExists(Dish dish, Long id, Restaurant restaurant) {
        if (dish == null) {
            throw new NotFoundException(DISH + ": " + id + " in " + restaurant);
        }
    }

    public static void checkDishDeleted(Dish dish, Restaurant restaurant) {
        if (dish.isDeleted()) {
            throw new DeletedEntityException(dish + " in " + restaurant);
        }
    }

    public static void checkLunchExists(Lunch lunch, Restaurant restaurant, LocalDate date) {
        if (lunch == null) {
            throw new NotFoundException(LUNCH + " from " + restaurant + " on " + date);
        }
    }

    public static void checkLunchAvailable(Lunch lunch, Restaurant restaurant, LocalDate date) {
        if (!lunch.isEnabled()) {
            throw new NotAvailableException(LUNCH + " from " + restaurant + " on " + date);
        }
    }

}
