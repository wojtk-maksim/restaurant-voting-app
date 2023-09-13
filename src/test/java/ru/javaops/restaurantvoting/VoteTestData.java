package ru.javaops.restaurantvoting;

import ru.javaops.restaurantvoting.web.VoteController;

import static ru.javaops.restaurantvoting.LunchTestData.NEW_DATE;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;

public class VoteTestData {

    public static final VoteController.SimpleVote NEW_VOTE = new VoteController.SimpleVote(NEW_DATE, BURGER_KING_ID);
}
