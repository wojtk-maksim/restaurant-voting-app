package ru.javaops.restaurantvoting.to;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Lunch;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ToConverter {

    public static LunchTo lunchTo(Lunch lunch) {
        return new LunchTo(
                lunch.getDate(),
                lunch.getRestaurant().getId(),
                lunch.getDishes().stream()
                        .map(d -> new SimpleDishTo(d.getName(), d.getPrice()))
                        .collect(Collectors.toSet()));
    }

    public static List<LunchTo> lunchTos(List<Lunch> lunches) {
        return lunches.stream()
                .map(ToConverter::lunchTo)
                .collect(Collectors.toList());
    }

    public static RestaurantDishTo restaurantDishTo(Dish dish) {
        return new RestaurantDishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static Set<RestaurantDishTo> dishTos(Set<Dish> dishes) {
        return dishes.stream()
                .map(ToConverter::restaurantDishTo)
                .collect(Collectors.toSet());
    }
}
