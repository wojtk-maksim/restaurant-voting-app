package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.HasName;
import ru.javaops.restaurantvoting.NamedEnablableDeletable;
import ru.javaops.restaurantvoting.error.DeletedEntityException;

public class DishUtil extends ValidationUtil {

    public static final String DISH_CODE = "dish";

    public static void checkDishFound(Object object, Object identifier, HasName restaurant) {
        checkFoundInRestaurant("dish", object, identifier, restaurant);
    }

    public static void checkDishDeleted(NamedEnablableDeletable dish, HasName restaurant) {
        if (dish.isDeleted()) {
            throw new DeletedEntityException(messageSourceAccessor.getMessage(
                    "error.deleted." + DISH_CODE,
                    new String[]{dish.getName(), restaurant.getName()}
            ));
        }
    }

}
