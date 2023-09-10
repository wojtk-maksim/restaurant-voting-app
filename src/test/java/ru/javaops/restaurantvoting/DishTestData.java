package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.dish.SimpleDishTo;

import java.util.List;

import static ru.javaops.restaurantvoting.RestaurantTestData.burgerKing;

public class DishTestData {

    public static final long BURGER_ID = 1;
    public static final String BURGER_NAME = "burger";

    public static final Dish burger = new Dish(BURGER_ID, "burger", 150, burgerKing);
    public static final Dish cheeseBurger = new Dish(2L, "cheeseburger", 200, burgerKing);
    public static final SimpleDishTo newDish = new SimpleDishTo("newDish", 100);
    public static final Dish savedDish = new Dish(2L, newDish.getName(), newDish.getPrice(), burgerKing);
    public static final Dish updatedDish = new Dish(BURGER_ID, newDish.getName(), newDish.getPrice(), burgerKing);

    public static final List<Dish> burgerKingDishes = List.of(burger, cheeseBurger);
}
