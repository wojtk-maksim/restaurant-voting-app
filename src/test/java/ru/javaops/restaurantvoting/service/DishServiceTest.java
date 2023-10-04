package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.repository.DishRepository;
import ru.javaops.restaurantvoting.to.DishTo;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.NOT_FOUND;

public class DishServiceTest extends AbstractTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishService dishService;

    @Test
    void getAllFromRestaurant() {
        DISH_TO_MATCHER.matches(dishService.getAllFromRestaurant(BURGER_KING_ID).getRestaurantItem(), burgerKingDishes);
    }

    @Test
    void getFromRestaurant() {
        DISH_TO_MATCHER.matches(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID).getRestaurantItem(), BURGER);
    }

    @Test
    void getFromRestaurantNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.getFromRestaurant(NOT_FOUND, BURGER_ID));
        assertThrows(NotFoundException.class, () -> dishService.getFromRestaurant(BURGER_KING_ID, NOT_FOUND));
    }

    @Test
    void add() {
        DishTo newDish = dishService.add(BURGER_KING_ID, NEW_DISH.getName(), NEW_DISH.getPrice());
        DISH_TO_MATCHER.matches(newDish, SAVED_DISH, "id");
        DISH_MATCHER.matches(dishRepository.get(BURGER_KING_ID, newDish.getId()), savedDish, "id");
    }

    @Test
    void enable() {
        dishService.enable(BURGER_KING_ID, BURGER_ID, false);
        assertFalse(dishRepository.get(BURGER_KING_ID, BURGER_ID).isEnabled());
    }

    @Test
    void update() {
        dishService.update(BURGER_KING_ID, BURGER_ID, NEW_DISH.getName(), NEW_DISH.getPrice());
        DISH_MATCHER.matches(dishRepository.get(BURGER_KING_ID, BURGER_ID), updatedDish);
    }

    @Test
    void delete() {
        dishService.softDelete(BURGER_KING_ID, BURGER_ID);
        assertTrue(dishRepository.get(BURGER_KING_ID, BURGER_ID).isDeleted());
    }

}
