package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.TestData.*;
import static ru.javaops.restaurantvoting.web.public_access.RestaurantController.RESTAURANTS_URL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(RESTAURANTS_URL + "/" + BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_TO_MATCHER.matches(result, BURGER_KING, "deleted"));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(RESTAURANTS_URL + "/" + NOT_FOUND)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getDeleted() throws Exception {
        perform(MockMvcRequestBuilders
                .get(RESTAURANTS_URL + "/" + DELETED_RESTAURANT.getId())
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isGone());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders
                .get(RESTAURANTS_URL)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> RESTAURANT_TO_MATCHER.matches(result, restaurantsExcludeDeleted, "deleted"));
    }

}

