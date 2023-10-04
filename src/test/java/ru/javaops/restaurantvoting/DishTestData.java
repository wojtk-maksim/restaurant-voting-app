package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.to.DishTo;

import java.util.List;

import static ru.javaops.restaurantvoting.RestaurantTestData.burgerKing;
import static ru.javaops.restaurantvoting.RestaurantTestData.kfc;

public class DishTestData {

    public static final Matcher<Dish> DISH_MATCHER = new Matcher<>(Dish.class, "restaurant");
    public static final Matcher<DishTo> DISH_TO_MATCHER = new Matcher<>(DishTo.class);

    public static final Long BURGER_ID = 1L;

    public static final Dish burger = new Dish(BURGER_ID, "Burger", 150, burgerKing);
    public static final Dish cheeseBurger = new Dish(3L, "Cheeseburger", 200, burgerKing);
    public static final Dish kfcDish = new Dish(6L, "Fries", 100, kfc);
    public static final DishTo BURGER = new DishTo(BURGER_ID, burger.getName(), burger.getPrice());
    public static final DishTo CHEESEBURGER = new DishTo(cheeseBurger.getId(), cheeseBurger.getName(), cheeseBurger.getPrice());
    public static final DishTo DELETED_DISH = new DishTo(2L, "Deleted Dish", 250, true, true);
    public static final DishTo UNAVAILABLE_DISH = new DishTo(4L, "Unavailable Dish", 199, false, false);
    public static final List<DishTo> burgerKingDishes = List.of(BURGER, CHEESEBURGER, UNAVAILABLE_DISH, DELETED_DISH);
    public static final List<DishTo> burgerKingDishesExcludeDeleted = List.of(BURGER, CHEESEBURGER, UNAVAILABLE_DISH);
    public static final DishTo KFC_DISH = new DishTo(kfcDish.getId(), kfcDish.getName(), kfcDish.getPrice());
    public static final DishTo DISH_FROM_UNAVAILABLE = new DishTo(7L, "Dish from Unavailable", 200);
    public static final DishTo DISH_FROM_DELETED = new DishTo(8L, "Dish from Deleted", 300);
    public static final DishTo NEW_DISH = new DishTo(null, "New Dish", 100, true, false);
    public static final Dish savedDish = new Dish(null, NEW_DISH.getName(), NEW_DISH.getPrice(), null);
    public static final DishTo SAVED_DISH = new DishTo(null, NEW_DISH.getName(), NEW_DISH.getPrice());
    public static final Dish updatedDish = new Dish(BURGER_ID, NEW_DISH.getName(), NEW_DISH.getPrice(), null);
    public static final DishTo UPDATED_DISH = new DishTo(BURGER_ID, NEW_DISH.getName(), NEW_DISH.getPrice());

}
