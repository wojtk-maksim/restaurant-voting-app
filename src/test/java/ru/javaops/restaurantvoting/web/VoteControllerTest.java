package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
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
import static ru.javaops.restaurantvoting.web.user.VoteController.VOTE_URL;

public class VoteControllerTest extends AbstractControllerTest {

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTE_URL, DATE_AS_STRING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertIterableEquals(lunches, jsonUtil.readValues(extractJson(result), LunchTo[].class)));
    }

    @Test
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(VOTE_URL, DATE_AS_STRING)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
