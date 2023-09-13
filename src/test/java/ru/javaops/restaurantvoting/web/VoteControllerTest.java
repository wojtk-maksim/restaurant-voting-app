package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVotersTo;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.VoteTestData.NEW_VOTE;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.VoteController.VOTING_URL;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchService lunchService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getOffersOnDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTING_URL + "/" + DATE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_WITH_VOTERS_MATCHER.matches(result, lunchesAvailableForVoting, LunchWithVotersTo[].class));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void vote() throws Exception {
        lunchService.add(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(NEW_VOTE)))
                .andDo(print())
                .andExpect(status().isOk());
        voteRepository.exists(NEW_DATE, ADMIN_ID, BURGER_KING_ID);
    }

}
