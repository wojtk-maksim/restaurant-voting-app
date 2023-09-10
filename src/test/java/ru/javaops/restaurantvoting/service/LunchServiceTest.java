package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.LunchTestData;
import ru.javaops.restaurantvoting.error.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.matches;

public class LunchServiceTest extends AbstractTest {

    @Autowired
    private LunchService lunchService;

    @Test
    void get() {
        matches(
                LunchTestData.burgerKingLunch,
                lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE),
                "id", "dishes.restaurant", "restaurant.dishes"
        );
    }

    @Test
    void add() {
        lunchService.add(BURGER_KING_ID, newLunch.getDate(), dishIdsForNewLunch);
        matches(
                lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE),
                newLunch,
                "id", "dishes.restaurant", "restaurant.dishes"
        );
    }

    @Test
    void update() {
        lunchService.update(BURGER_KING_ID, updatedLunch.getDate(), dishIdsForUpdatedLunch);
        matches(
                lunchService.getFromRestaurantOnDate(BURGER_KING_ID, updatedLunch.getDate()),
                updatedLunch,
                "id", "dishes.restaurant", "restaurant"
        );
    }

    @Test
    void delete() {
        lunchService.delete(BURGER_KING_ID, DATE);
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE));
    }

}
