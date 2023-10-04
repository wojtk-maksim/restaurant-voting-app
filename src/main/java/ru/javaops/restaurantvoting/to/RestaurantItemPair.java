package ru.javaops.restaurantvoting.to;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantItemPair<T> {

    private final RestaurantTo restaurant;

    private final T restaurantItem;

}
