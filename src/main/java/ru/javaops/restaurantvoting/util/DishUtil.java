package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.dish.DishTo;

import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.*;

public class DishUtil {

    public static final String DISH = "Dish";

    public static void checkDishExists(Dish dish, Long restaurantId, Long id) {
        checkExistsInRestaurant(dish, DISH, restaurantId, id);
    }

    public static void checkDishExists(DishTo dish, Long restaurantId, Long id) {
        checkExistsInRestaurant(dish, DISH, restaurantId, id);
    }

    public static void checkDishDeleted(Dish dish, Long restaurantId, Long id) {
        checkDeletedInRestaurant(dish, DISH, restaurantId, id);
    }

    public static void checkDishAvailable(DishTo dish, Long restaurantId, Long id) {
        checkExistsInRestaurant(dish, DISH, restaurantId, id);
        checkAvailableInRestaurant(dish, DISH, restaurantId, id);
    }

    public static DishTo dishTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.isEnabled(), dish.isDeleted());
    }

}
