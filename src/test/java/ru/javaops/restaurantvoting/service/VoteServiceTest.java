package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.repository.VoteRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_ID;

public class VoteServiceTest extends AbstractTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private LunchService lunchService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getOffersOnDate() {
        LUNCH_WITH_VOTERS_MATCHER.matches(voteService.getOffersOnDate(DATE), lunchesAvailableForVoting);
    }

    @Test
    void vote() {
        lunchService.add(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        voteService.vote(NEW_DATE, ADMIN_ID, BURGER_KING_ID);
        assertTrue(voteRepository.exists(NEW_DATE, ADMIN_ID, BURGER_KING_ID));
    }

}
