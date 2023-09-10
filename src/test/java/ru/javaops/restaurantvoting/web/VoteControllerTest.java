package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.DATE;
import static ru.javaops.restaurantvoting.LunchTestData.lunchesOnDate;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.TestUtil.parseObjects;
import static ru.javaops.restaurantvoting.UserTestData.ADMIN_EMAIL;
import static ru.javaops.restaurantvoting.UserTestData.USER_EMAIL;
import static ru.javaops.restaurantvoting.VoteTestData.NEW_VOTE;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.VoteController.VOTING_URL;

public class VoteControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAllOnDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTING_URL + "/" + DATE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObjects(result, LunchWithVotersTo[].class), lunchesOnDate, "restaurant.enabled", "voters.lunchId"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void vote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(NEW_VOTE)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
