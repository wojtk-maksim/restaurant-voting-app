package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.restaurant.NewRestaurantTo;

import java.util.List;

public class RestaurantTestData {

    public static final long BURGER_KING_ID = 1;

    public static final Restaurant burgerKing = new Restaurant(BURGER_KING_ID, "Burger King");
    public static final Restaurant kfc = new Restaurant(2L, "KFC");
    public static final Restaurant subway = new Restaurant(3L, "Subway", false);
    public static final Restaurant dodoPizza = new Restaurant(4L, "Dodo Pizza");
    public static final NewRestaurantTo newRestaurant = new NewRestaurantTo("newRestaurant");
    public static final Restaurant savedRestaurant = new Restaurant(newRestaurant.name());
    public static final Restaurant updatedRestaurant = new Restaurant(BURGER_KING_ID, newRestaurant.name());

    public static final List<Restaurant> restaurants = List.of(burgerKing, dodoPizza, kfc, subway);
    public static final List<Restaurant> availableRestaurants = List.of(burgerKing, dodoPizza, kfc);
}
