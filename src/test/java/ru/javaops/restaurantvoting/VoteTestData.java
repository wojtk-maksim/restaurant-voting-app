package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Vote;

import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.burgerKing;
import static ru.javaops.restaurantvoting.UserTestData.admin;

public class VoteTestData {

    public static final Vote adminVote = new Vote(DATE, admin, new Lunch(burgerKingLunch.date(), burgerKing, burgerKingLunchDishes));
}
