package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.getBurgerKing;

public class LunchServiceTest extends AbstractTest {

    @Autowired
    private LunchService lunchService;

    @Test
    void getAllOnDate() {
        LUNCH_MATCHER.matches(new ArrayList<>(lunchService.getAllOnDate(DATE).values()), allLunches);
    }

    @Test
    void get() {
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(getBurgerKing(), DATE), burgerKingLunch);
    }

    @Test
    void add() {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(getBurgerKing(), NEW_DATE), newLunch);
    }

    @Test
    void update() {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        lunchService.update(getBurgerKing(), NEW_DATE, dishIdsForUpdatedLunch);
        LUNCH_MATCHER.matches(lunchService.getFromRestaurantOnDate(getBurgerKing(), NEW_DATE), updatedLunch);
    }

    @Test
    void delete() {
        lunchService.delete(getBurgerKing(), DATE);
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(getBurgerKing(), DATE));
    }

}
