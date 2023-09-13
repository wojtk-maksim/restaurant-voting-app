package ru.javaops.restaurantvoting.util;

import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.restaurant.RestaurantTo;

import static ru.javaops.restaurantvoting.util.validation.ValidationUtil.*;

public class RestaurantUtil {

    public static final String RESTAURANT = "Restaurant";
    public static final String DISH = "Dish";

    private static RestaurantService restaurantService;

    public static void setRestaurantService(RestaurantService restaurantService) {
        RestaurantUtil.restaurantService = restaurantService;
    }

    public static Restaurant getRestaurantIfExists(Long id) {
        Restaurant restaurant = restaurantService.getAll().get(id);
        checkRestaurantExists(restaurant, id);
        return restaurant;
    }

    public static RestaurantTo restaurantTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.isEnabled(), restaurant.isDeleted());
    }

    public static void checkRestaurantExists(Restaurant restaurant, Long id) {
        checkExists(restaurant, RESTAURANT, id);
    }

    public static void checkRestaurantExists(RestaurantTo restaurant, Long id) {
        checkExists(restaurant, RESTAURANT, id);
    }

    public static void checkRestaurantDeleted(RestaurantTo restaurant) {
        checkDeleted(restaurant, RESTAURANT, restaurant.getId());
    }

    public static void checkRestaurantDeleted(Restaurant restaurant) {
        checkDeleted(restaurant, RESTAURANT, restaurant.getId());
    }

    public static void checkRestaurantAvailable(Restaurant restaurant) {
        checkAvailable(restaurant, RESTAURANT, restaurant.getId());
    }

    public static void checkRestaurantAvailable(RestaurantTo restaurant) {
        checkAvailable(restaurant, RESTAURANT, restaurant.getId());
    }

}
