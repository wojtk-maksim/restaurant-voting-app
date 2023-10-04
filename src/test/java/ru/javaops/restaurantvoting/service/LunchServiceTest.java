package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.DeadLineException;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.LunchRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.restaurantvoting.DishTestData.CHEESEBURGER;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;

public class LunchServiceTest extends AbstractTest {

    @Autowired
    private LunchRepository lunchRepository;

    @Autowired
    private LunchService lunchService;

    @Test
    void getFromRestaurantOnDate() {
        LUNCH_TO_MATCHER.matches(lunchService.getFromRestaurantOnDate(BURGER_KING_ID, DATE).getRestaurantItem(), BURGER_KING_LUNCH);
    }

    @Test
    void getFromRestaurantOnDateNotFound() {
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(NOT_FOUND, DATE));
        assertThrows(NotFoundException.class, () -> lunchService.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE));
    }

    @Test
    void create() {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        LUNCH_MATCHER.matches(lunchRepository.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE), newLunch, "id");
    }

    @Test
    void createAfterDeadline() {
        assertThrows(DeadLineException.class, () -> lunchService.createOrUpdate(BURGER_KING_ID, DATE, dishIdsForNewLunch));
    }

    @Test
    void update() {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, List.of(CHEESEBURGER.getId()));
        LUNCH_MATCHER.matches(lunchRepository.getFromRestaurantOnDate(BURGER_KING_ID, NEW_DATE), updatedLunch, "id");
    }

    @Test
    void enable() {
        lunchService.enable(BURGER_KING_ID, DATE, false);
        assertFalse(lunchRepository.getFromRestaurantOnDate(BURGER_KING_ID, DATE).isEnabled());
    }

    @Test
    void delete() {
        lunchService.delete(BURGER_KING_ID, DATE);
        assertNull(lunchRepository.getFromRestaurantOnDate(BURGER_KING_ID, DATE));
    }

}
