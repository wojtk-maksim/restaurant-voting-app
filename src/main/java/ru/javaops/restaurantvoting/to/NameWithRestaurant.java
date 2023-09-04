package ru.javaops.restaurantvoting.to;

import ru.javaops.restaurantvoting.model.Restaurant;

public record NameWithRestaurant(String name, Restaurant restaurant) {
}
