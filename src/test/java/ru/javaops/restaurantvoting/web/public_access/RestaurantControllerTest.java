package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Restaurant;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.RestaurantTestData.*;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.web.public_access.RestaurantController.RESTAURANTS_URL;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANTS_URL + "/" + BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertFalse(result.getResponse().getContentAsString().contains("deleted")))
                .andExpect(result -> RESTAURANT_MATCHER.matches(result, getBurgerKing(), Restaurant.class));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESTAURANTS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertFalse(result.getResponse().getContentAsString().contains("deleted")))
                .andExpect(result -> RESTAURANT_MATCHER.matches(result, restaurantsExceptDeleted, Restaurant[].class));
    }

}

