package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.RestaurantService;

public class RestaurantUtil {

    private static RestaurantService restaurantService;

    public static Restaurant getRestaurant(long id) {
        return restaurantService.getAll().get(id);
    }

    public static void setRestaurantService(RestaurantService restaurantService) {
        RestaurantUtil.restaurantService = restaurantService;
    }

}
