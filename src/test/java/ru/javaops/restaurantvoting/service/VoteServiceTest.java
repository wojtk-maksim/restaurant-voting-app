package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.repository.VoteRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.LunchTestData.DATE;
import static ru.javaops.restaurantvoting.LunchTestData.lunchesOnDate;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_ID;
import static ru.javaops.restaurantvoting.UserTestData.admin;

public class VoteServiceTest extends AbstractTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getOffersOnDate() {
        matches(voteService.getOffersOnDate(DATE), lunchesOnDate, "restaurant.dishes");
    }

    @Test
    void vote() {
        voteService.vote(DATE, admin, BURGER_KING_ID);
        assertTrue(voteRepository.checkVote(DATE, ADMIN_ID, BURGER_KING_ID));
    }

}
