package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Restaurant;

import java.util.List;

public class RestaurantTestData {

    public static final int BURGER_KING_ID = 1;

    public static final Restaurant burgerKing = new Restaurant(BURGER_KING_ID, "Burger King");
    public static final Restaurant kfc = new Restaurant(2, "KFC");
    public static final Restaurant subway = new Restaurant(3, "Subway");
    public static final Restaurant dodoPizza = new Restaurant(4, "Dodo Pizza");
    private static final Restaurant newRestaurant = new Restaurant("newRestaurant");
    private static final Restaurant updatedRestaurant = new Restaurant(BURGER_KING_ID, "updatedRestaurant");

    public static final List<Restaurant> restaurants = List.of(burgerKing, dodoPizza, kfc, subway);

    public static Restaurant getNew() {
        return newRestaurant;
    }

    public static Restaurant getUpdated() {
        return updatedRestaurant;
    }
}
