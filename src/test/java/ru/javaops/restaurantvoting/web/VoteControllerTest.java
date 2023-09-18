package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.to.lunch.LunchWithVoters;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.RestaurantTestData.getBurgerKing;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.VoteController.VOTING_URL;

public class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchService lunchService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getAvailableOffersOnDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(VOTING_URL + "/" + DATE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_WITH_VOTERS_MATCHER.matches(result, lunchesAvailableForVoting, LunchWithVoters[].class));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void vote() throws Exception {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/" + NEW_DATE + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isOk());
        voteRepository.exists(NEW_DATE, ADMIN_ID, BURGER_KING_ID);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void voteAfterDeadline() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/" + DATE + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void voteForUnavailableLunch() throws Exception {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        lunchService.enable(getBurgerKing(), NEW_DATE, false);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/" + NEW_DATE + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void voteForUnavailableRestaurant() throws Exception {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        restaurantService.enable(getBurgerKing(), false);
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/" + NEW_DATE + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void voteForDeletedRestaurant() throws Exception {
        lunchService.add(getBurgerKing(), NEW_DATE, dishIdsForNewLunch);
        restaurantService.delete(getBurgerKing());
        mockMvc.perform(MockMvcRequestBuilders.post(VOTING_URL + "/" + NEW_DATE + "/vote")
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isGone());
    }

}
