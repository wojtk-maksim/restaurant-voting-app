package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.service.DishService;

import static ru.javaops.restaurantvoting.util.ValidationUtil.checkDishExists;

public abstract class AbstractDishController extends AbstractRestaurantController {

    @Autowired
    protected DishService dishService;

    protected Dish getDishIfExists(Restaurant restaurant, Long id) {
        Dish dish = dishService.getAllFromRestaurant(restaurant).get(id);
        checkDishExists(dish, id, restaurant);
        return dish;
    }

}
