package ru.javaops.restaurantvoting.to;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;

public record VoteValidationObject(Restaurant restaurant, Lunch lunch, Vote vote, User user) {

}
