package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.RestaurantRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;

public class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void getAll() {
        RESTAURANT_TO_MATCHER.matches(restaurantService.getAll(), restaurants);
    }

    @Test
    void get() {
        RESTAURANT_TO_MATCHER.matches(restaurantService.get(BURGER_KING_ID), BURGER_KING);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(NOT_FOUND));
    }

    @Test
    void add() {
        RESTAURANT_TO_MATCHER.matches(restaurantService.add(NEW_RESTAURANT.getName()), SAVED_RESTAURANT, "id");
    }

    @Test
    void update() {
        restaurantService.update(BURGER_KING_ID, NEW_RESTAURANT.getName());
        RESTAURANT_MATCHER.matches(restaurantRepository.get(BURGER_KING_ID), updatedRestaurant);
    }

    @Test
    void enable() {
        restaurantService.enable(BURGER_KING_ID, false);
        assertFalse(restaurantRepository.get(BURGER_KING_ID).isEnabled());
    }

    @Test
    void softDelete() {
        restaurantService.softDelete(BURGER_KING_ID);
        assertTrue(restaurantRepository.get(BURGER_KING_ID).isDeleted());
    }

    @Test
    void hardDelete() {
        restaurantService.hardDelete(BURGER_KING_ID);
        assertNull(restaurantRepository.get(BURGER_KING_ID));
    }

}
