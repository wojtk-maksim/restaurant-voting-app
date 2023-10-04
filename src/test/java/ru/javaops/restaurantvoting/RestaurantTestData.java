package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.RestaurantTo;

import java.util.List;

public class RestaurantTestData {
    public static final Matcher<Restaurant> RESTAURANT_MATCHER = new Matcher<>(Restaurant.class, "dishes");
    public static final Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = new Matcher<>(RestaurantTo.class);

    public static final Long BURGER_KING_ID = 3L;

    public static final Restaurant burgerKing = new Restaurant(BURGER_KING_ID, "Burger King");
    public static final Restaurant kfc = new Restaurant(4L, "KFC");
    public static final RestaurantTo BURGER_KING = new RestaurantTo(BURGER_KING_ID, burgerKing.getName(), true, false);
    public static final RestaurantTo KFC = new RestaurantTo(kfc.getId(), kfc.getName());
    public static final RestaurantTo UNAVAILABLE_RESTAURANT = new RestaurantTo(2L, "Unavailable Restaurant", false, false);
    public static final RestaurantTo DELETED_RESTAURANT = new RestaurantTo(1L, "Deleted Restaurant", true, true);
    public static final List<RestaurantTo> restaurants = List.of(BURGER_KING, KFC, UNAVAILABLE_RESTAURANT, DELETED_RESTAURANT);
    public static final List<RestaurantTo> restaurantsExcludeDeleted = List.of(BURGER_KING, KFC, UNAVAILABLE_RESTAURANT);
    public static final RestaurantTo NEW_RESTAURANT = new RestaurantTo(null, "newRestaurant");
    public static final RestaurantTo SAVED_RESTAURANT = new RestaurantTo(null, NEW_RESTAURANT.getName());
    public static final Restaurant updatedRestaurant = new Restaurant(BURGER_KING_ID, NEW_RESTAURANT.getName());
    public static final RestaurantTo UPDATED_RESTAURANT = new RestaurantTo(BURGER_KING_ID, NEW_RESTAURANT.getName());


}
