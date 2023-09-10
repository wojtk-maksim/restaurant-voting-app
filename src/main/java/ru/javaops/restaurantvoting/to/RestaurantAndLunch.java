package ru.javaops.restaurantvoting.to;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.util.validation.restaurant.AvailableRestaurant;

public record RestaurantAndLunch(@AvailableRestaurant Restaurant restaurant, Lunch lunch) {
}
