package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.LunchTestData;
import ru.javaops.restaurantvoting.error.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;

public class LunchServiceTest extends AbstractTest {

    @Autowired
    private LunchService lunchService;

    @Test
    void get() {
        assertEquals(LunchTestData.burgerKingLunch, lunchService.get(BURGER_KING_ID, DATE));
    }

    @Test
    void add() {
        lunchService.add(newLunch.restaurantId(), newLunch.date(), dishIdsForNewLunch);
        assertEquals(newLunch, lunchService.get(BURGER_KING_ID, NEW_DATE));
    }

    @Test
    void update() {
        lunchService.update(updatedLunch.restaurantId(), updatedLunch.date(), dishIdsForUpdatedLunch);
        assertEquals(updatedLunch, lunchService.get(BURGER_KING_ID, DATE));
    }

    @Test
    void delete() {
        lunchService.delete(BURGER_KING_ID, DATE);
        assertThrows(NotFoundException.class, () -> lunchService.get(BURGER_KING_ID, DATE));
    }
}
