package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.to.LunchTo;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.DATE_AS_STRING;
import static ru.javaops.restaurantvoting.LunchTestData.lunches;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestUtil.extractJson;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.util.JsonUtil.readValues;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.VoteController.VOTE_URL;

public class VoteControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL, DATE_AS_STRING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertIterableEquals(lunches, readValues(extractJson(result), LunchTo[].class)));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(VOTE_URL, DATE_AS_STRING)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
