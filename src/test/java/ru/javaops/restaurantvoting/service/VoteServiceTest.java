package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.VoteRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaops.restaurantvoting.LunchTestData.DATE;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.UserTestData.admin;
import static ru.javaops.restaurantvoting.VoteTestData.adminVote;

public class VoteServiceTest extends AbstractTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void vote() {
        voteService.vote(DATE, admin, BURGER_KING_ID);
        Vote vote = voteRepository.findById(new Vote.VoteId(DATE, admin)).orElseThrow(NotFoundException::new);
        assertEquals(adminVote, vote);
    }
}
