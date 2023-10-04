package ru.javaops.restaurantvoting.web.admin_access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.TestData;
import ru.javaops.restaurantvoting.error.NotFoundException;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.util.JwtUtil;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestData.*;
import static ru.javaops.restaurantvoting.UserTestData.admin;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.admin_access.AdminRestaurantController.ADMIN_RESTAURANTS_URL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_RESTAURANTS_URL)
                .header("Authorization", "Bearer " + JwtUtil.generateToken(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_TO_MATCHER.matches(result, restaurants));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_TO_MATCHER.matches(result, BURGER_KING));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(ADMIN_RESTAURANTS_URL + "/" + NOT_FOUND)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void addNew() throws Exception {
        perform(MockMvcRequestBuilders
                .post(ADMIN_RESTAURANTS_URL)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(NEW_RESTAURANT)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_TO_MATCHER.matches(result, SAVED_RESTAURANT, "id"));
    }

    @Test
    void addNewNameExists() throws Exception {
        perform(MockMvcRequestBuilders
                .post(ADMIN_RESTAURANTS_URL)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(BURGER_KING)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders
                .put(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(NEW_RESTAURANT)))
                .andDo(print())
                .andExpect(status().isOk());
        em.clear();
        RESTAURANT_TO_MATCHER.matches(restaurantService.get(BURGER_KING_ID), UPDATED_RESTAURANT);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders
                .patch(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT)
                .param("enabled", "false"))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(restaurantService.get(BURGER_KING_ID).isEnabled());
    }

    @Test
    void softDelete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(restaurantService.get(BURGER_KING_ID).isDeleted());
    }

    @Test
    void hardDelete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(ADMIN_RESTAURANTS_URL + "/" + BURGER_KING_ID + "/hard-delete")
                .header(TestData.AUTHORIZATION_HEADER, SUPER_ADMIN_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertThrows(NotFoundException.class, () -> restaurantService.get(BURGER_KING_ID));
    }

}
