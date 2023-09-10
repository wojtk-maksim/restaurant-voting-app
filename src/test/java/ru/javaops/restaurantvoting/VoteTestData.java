package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.web.VoteController;

import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.burgerKing;
import static ru.javaops.restaurantvoting.RestaurantTestData.kfc;
import static ru.javaops.restaurantvoting.UserTestData.admin;

public class VoteTestData {

    public static final Vote adminVote = new Vote(DATE, admin, new Lunch(burgerKingLunch.getDate(), burgerKing, burgerKingLunchDishes));

    public static final VoteController.SimpleVote NEW_VOTE = new VoteController.SimpleVote(DATE, kfc.getId());
}
