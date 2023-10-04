package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.AUTHORIZATION_HEADER;
import static ru.javaops.restaurantvoting.TestData.USER_JWT;
import static ru.javaops.restaurantvoting.web.public_access.LunchController.LUNCHES_URL;

public class LunchControllerTest extends AbstractControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders
                .get(LUNCHES_URL + "/" + DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_TO_MATCHER.matches(result, BURGER_KING_LUNCH));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(LUNCHES_URL + "/" + NEW_DATE, BURGER_KING_ID)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
