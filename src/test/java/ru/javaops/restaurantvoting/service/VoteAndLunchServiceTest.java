package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.DeletedEntityException;
import ru.javaops.restaurantvoting.error.NotAvailableException;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.LunchRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.DishTestData.DISH_FROM_DELETED;
import static ru.javaops.restaurantvoting.DishTestData.DISH_FROM_UNAVAILABLE;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;

public class VoteAndLunchServiceTest extends AbstractTest {

    @Autowired
    private VoteAndLunchService voteAndLunchService;

    @Autowired
    private LunchRepository lunchRepository;

    @Autowired
    private LunchService lunchService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getAllLunchesOnDate() {
        LUNCH_WITH_VOTERS_MATCHER.matches(voteAndLunchService.getLunchesForVoting(DATE), allLunchesOnDate);
    }

    @Test
    void vote() {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        voteAndLunchService.vote(NEW_DATE, USER_ID, BURGER_KING_ID);
        assertTrue(voteRepository.exists(NEW_DATE, USER_ID, BURGER_KING_ID));
    }

    @Test
    void voteRestaurantNotFound() {
        assertThrows(NotFoundException.class, () -> voteAndLunchService.vote(NEW_DATE, USER_ID, NOT_FOUND));
    }

    @Test
    void voteRestaurantNotAvailable() {
        lunchService.createOrUpdate(UNAVAILABLE_RESTAURANT.getId(), NEW_DATE, List.of(DISH_FROM_UNAVAILABLE.getId()));
        assertThrows(NotAvailableException.class, () -> voteAndLunchService.vote(NEW_DATE, USER_ID, UNAVAILABLE_RESTAURANT.getId()));
    }

    @Test
    void voteRestaurantDeleted() {
        lunchService.createOrUpdate(DELETED_RESTAURANT.getId(), NEW_DATE, List.of(DISH_FROM_DELETED.getId()));
        assertThrows(DeletedEntityException.class, () -> voteAndLunchService.vote(NEW_DATE, USER_ID, DELETED_RESTAURANT.getId()));
    }

    @Test
    void voteLunchNotAvailable() {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        lunchService.enable(BURGER_KING_ID, NEW_DATE, false);
        em.clear();
        assertThrows(NotAvailableException.class, () -> voteAndLunchService.vote(NEW_DATE, USER_ID, BURGER_KING_ID));
    }

    @Test
    void batchCreate() {
        voteAndLunchService.batchCreateOrUpdate(
                NEW_DATE,
                DISHES_FOR_MULTIPLE_LUNCHES.getDishIdsByRestaurantIds(),
                DISHES_FOR_MULTIPLE_LUNCHES.getAllRestaurantIds(),
                DISHES_FOR_MULTIPLE_LUNCHES.getAllDishIds()
        );
        LUNCH_MATCHER.matches(lunchRepository.getAllOnDateForCache(NEW_DATE), newLunches, "id");
    }

}
