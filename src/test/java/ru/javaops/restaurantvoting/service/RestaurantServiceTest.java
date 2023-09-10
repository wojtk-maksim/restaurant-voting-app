package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestUtil.matches;

public class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getAll() {
        matches(new ArrayList<>(restaurantService.getAll().values()), restaurants, "dishes");
    }

    @Test
    void get() {
        matches(restaurantService.get(BURGER_KING_ID), burgerKing, "dishes");
    }

    @Test
    void add() {
        matches(restaurantService.add(newRestaurant), savedRestaurant, "id", "dishes");
    }

    @Test
    void update() {
        restaurantService.update(BURGER_KING_ID, newRestaurant);
        matches(restaurantService.get(BURGER_KING_ID), updatedRestaurant, "dishes");
    }

    @Test
    void enable() {
        restaurantService.enable(BURGER_KING_ID, false);
        assertFalse(restaurantService.get(BURGER_KING_ID).isEnabled());
    }

    @Test
    void delete() {

    }
}
