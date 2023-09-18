package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.to.SimpleRestaurant;

import java.util.List;

public class RestaurantTestData {

    public static final TestUtil.Matcher<Restaurant> RESTAURANT_MATCHER = new TestUtil.Matcher<>(List.of("dishes"));

    public static final long BURGER_KING_ID = 1;

    //public static final Restaurant burgerKing = new Restaurant(BURGER_KING_ID, "Burger King");
    public static Restaurant getBurgerKing() {
        return new Restaurant(BURGER_KING_ID, "Burger King");
    }

    public static final Restaurant kfc = new Restaurant(2L, "KFC");
    public static final Restaurant unavailableRestaurant = new Restaurant(3L, "Unavailable Restaurant", false);
    public static final Restaurant deletedRestaurant = new Restaurant(4L, "Deleted Restaurant", true, true);
    public static final List<Restaurant> restaurants = List.of(getBurgerKing(), kfc, unavailableRestaurant, deletedRestaurant);
    public static final List<Restaurant> restaurantsExceptDeleted = List.of(getBurgerKing(), kfc, unavailableRestaurant);
    public static final SimpleRestaurant newRestaurant = new SimpleRestaurant("newRestaurant");
    public static final Restaurant savedRestaurant = new Restaurant(newRestaurant.getName());
    public static final Restaurant updatedRestaurant = new Restaurant(BURGER_KING_ID, newRestaurant.getName());

}
