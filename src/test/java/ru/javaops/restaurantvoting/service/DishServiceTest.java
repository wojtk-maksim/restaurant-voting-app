package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.DishRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.NOT_FOUND;

public class DishServiceTest extends AbstractTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Test
    void getAll() {
        DISH_MATCHER.matches(new ArrayList<>(dishService.getAllFromRestaurant(BURGER_KING_ID).values()), burgerKingDishes);
    }

    @Test
    void get() {
        DISH_MATCHER.matches(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID), burger);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.getFromRestaurant(BURGER_KING_ID, NOT_FOUND));
    }

    @Test
    void add() {
        DISH_MATCHER.matches(dishService.add(BURGER_KING_ID, newDish), savedDish, "id");
    }

    @Test
    void update() {
        dishService.update(BURGER_KING_ID, BURGER_ID, newDish);
        DISH_MATCHER.matches(dishRepository.get(BURGER_KING_ID, BURGER_ID), updatedDish);
    }

    @Test
    void delete() {
        dishService.delete(BURGER_KING_ID, BURGER_ID);
        assertTrue(dishRepository.get(BURGER_KING_ID, BURGER_ID).isDeleted());
    }

}
