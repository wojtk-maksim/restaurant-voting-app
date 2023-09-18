package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getAll() {
        RESTAURANT_MATCHER.matches(new ArrayList<>(restaurantService.getAll().values()), restaurants);
    }

    @Test
    void get() {
        RESTAURANT_MATCHER.matches(restaurantService.get(BURGER_KING_ID), getBurgerKing());
    }

    @Test
    void add() {
        RESTAURANT_MATCHER.matches(restaurantService.add(newRestaurant), savedRestaurant, "id");
    }

    @Test
    void update() {
        restaurantService.update(getBurgerKing(), newRestaurant);
        RESTAURANT_MATCHER.matches(restaurantService.get(BURGER_KING_ID), updatedRestaurant);
    }

    @Test
    void enable() {
        restaurantService.enable(getBurgerKing(), false);
        assertFalse(restaurantRepository.get(BURGER_KING_ID).isEnabled());
    }

    @Test
    void delete() {
        restaurantService.delete(getBurgerKing());
        assertTrue(restaurantRepository.get(BURGER_KING_ID).isDeleted());
    }

}
