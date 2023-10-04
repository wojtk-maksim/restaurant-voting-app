package ru.javaops.restaurantvoting.web.admin_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.service.DishService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.DishTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.*;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin_access.AdminDishController.ADMIN_DISHES_URL;

public class AdminDishControllerTest extends AbstractControllerTest {

    @Autowired
    private DishService dishService;

    @Test
    void getAllFromRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_DISHES_URL, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> DISH_TO_MATCHER.matches(result, burgerKingDishes));
    }

    @Test
    void getFromRestaurant() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> DISH_TO_MATCHER.matches(result, BURGER));
    }

    @Test
    void getFromRestaurantNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_DISHES_URL + "/" + NOT_FOUND, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders
                .post(ADMIN_DISHES_URL, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(NEW_DISH)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> DISH_TO_MATCHER.matches(result, SAVED_DISH, "id"));
    }

    @Test
    void createNameExists() throws Exception {
        perform(MockMvcRequestBuilders
                .post(ADMIN_DISHES_URL, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(BURGER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders
                .put(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(UPDATED_DISH)))
                .andDo(print())
                .andExpect(status().isOk());
        DISH_TO_MATCHER.matches(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID).getRestaurantItem(), UPDATED_DISH);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID).getRestaurantItem().isEnabled());
    }

    @Test
    void softDelete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(ADMIN_DISHES_URL + "/" + BURGER_ID, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID).getRestaurantItem().isDeleted());
    }

    @Test
    void hardDelete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(ADMIN_DISHES_URL + "/" + BURGER_ID + "/hard-delete", BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, SUPER_ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> dishService.getFromRestaurant(BURGER_KING_ID, BURGER_ID));
    }

}
