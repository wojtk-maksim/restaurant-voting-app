package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;

public class LunchServiceTest extends AbstractTest {

    @Autowired
    private LunchService lunchService;

    @Test
    void getAllOnDate() {
        LUNCH_MATCHER.matches(lunchService.getAllOnDate(DATE), allLunches);
    }

    @Test
    void get() {
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE), burgerKingLunch);
    }

    @Test
    void add() {
        lunchService.add(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE), newLunch);
    }

    @Test
    void update() {
        lunchService.add(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        lunchService.update(BURGER_KING_ID, NEW_DATE, dishIdsForUpdatedLunch);
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE), updatedLunch);
    }

    @Test
    void delete() {
        lunchService.delete(BURGER_KING_ID, DATE);
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE));
    }

}
