package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.RestaurantService;

import static ru.javaops.restaurantvoting.util.ValidationUtil.checkRestaurantExists;

public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantService restaurantService;

    protected Restaurant getRestaurantIfExists(Long id) {
        Restaurant restaurant = restaurantService.getAll().get(id);
        checkRestaurantExists(restaurant, id);
        return restaurant;
    }

}
