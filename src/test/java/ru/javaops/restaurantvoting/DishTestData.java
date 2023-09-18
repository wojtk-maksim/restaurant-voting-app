package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.NewDishTo;

import java.util.List;

import static ru.javaops.restaurantvoting.RestaurantTestData.*;

public class DishTestData {

    public static final TestUtil.Matcher<Dish> DISH_MATCHER = new TestUtil.Matcher<>(List.of("restaurant"));

    public static final long BURGER_ID = 1;

    public static final Dish burger = new Dish(BURGER_ID, "Burger", 150, getBurgerKing());

    public static Dish getBurger() {
        return new Dish(BURGER_ID, "Burger", 150, getBurgerKing());
    }

    public static final Dish cheeseBurger = new Dish(3L, "Cheeseburger", 200, getBurgerKing());
    public static final Dish deletedDish = new Dish(2L, "Deleted Dish", 250, getBurgerKing(), true, true);
    public static final Dish unavailableDish = new Dish(4L, "Unavailable Dish", 199, getBurgerKing(), false, false);
    public static final List<Dish> burgerKingDishes = List.of(burger, cheeseBurger, unavailableDish, deletedDish);
    public static final List<Dish> burgerKingDishesExceptDeleted = List.of(burger, cheeseBurger, unavailableDish);
    public static final Dish kfcDish = new Dish(6L, "Fries", 100, kfc);
    public static final Dish dishFromUnavailable = new Dish(7L, "Dish from Unavailable", 200, unavailableRestaurant);
    public static final Dish dishFromDeleted = new Dish(8L, "Dish from Deleted", 300, deletedRestaurant);
    public static final NewDishTo newDish = new NewDishTo("New Dish", 100);
    public static final Dish savedDish = new Dish(newDish.getName(), newDish.getPrice(), getBurgerKing());
    public static final Dish updatedDish = new Dish(BURGER_ID, newDish.getName(), newDish.getPrice(), getBurgerKing());

}
