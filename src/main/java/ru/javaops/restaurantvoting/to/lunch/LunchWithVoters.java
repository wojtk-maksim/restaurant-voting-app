package ru.javaops.restaurantvoting.to.lunch;

import ru.javaops.restaurantvoting.model.Dish;
import ru.javaops.restaurantvoting.model.Restaurant;

import java.util.List;

public record LunchWithVoters(
        Restaurant restaurant,
        List<Dish> dishes,
        List<VoterTo> voters) {
}
