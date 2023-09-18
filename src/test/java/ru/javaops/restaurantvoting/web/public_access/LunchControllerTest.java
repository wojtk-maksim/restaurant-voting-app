package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.Lunch;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.web.public_access.LunchController.LUNCHES_URL;

public class LunchControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LUNCHES_URL + "/" + DATE, BURGER_KING_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_MATCHER.matches(result, burgerKingLunch, Lunch.class));
    }

}
