package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.RestaurantDishTo;
import ru.javaops.restaurantvoting.to.SimpleDishTo;

import java.util.List;

import static ru.javaops.restaurantvoting.RestaurantTestData.burgerKing;
import static ru.javaops.restaurantvoting.to.ToConverter.restaurantDishTo;

public class DishTestData {

    public static final Integer BURGER_ID = 1;
    public static final String BURGER_NAME = "burger";

    public static final Dish burger = new Dish(BURGER_ID, "burger", 150, burgerKing);
    public static final Dish cheeseBurger = new Dish(2, "cheeseburger", 200, burgerKing);
    public static final SimpleDishTo newDish = new SimpleDishTo("newDish", 100);
    public static final SimpleDishTo updatedDish = new SimpleDishTo("updatedDish", 300);

    public static final List<RestaurantDishTo> burgerKingDishes = List.of(restaurantDishTo(burger), restaurantDishTo(cheeseBurger));

    public static SimpleDishTo getNew() {
        return newDish;
    }

    public static SimpleDishTo getUpdated() {
        return updatedDish;
    }
}
