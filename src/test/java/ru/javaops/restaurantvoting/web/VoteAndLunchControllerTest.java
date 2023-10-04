package ru.javaops.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.TestData;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.service.LunchService;
import ru.javaops.restaurantvoting.service.RestaurantService;
import ru.javaops.restaurantvoting.service.VoteAndLunchService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.LunchTestData.*;
import static ru.javaops.restaurantvoting.RestaurantTestData.BURGER_KING_ID;
import static ru.javaops.restaurantvoting.TestData.ADMIN_JWT;
import static ru.javaops.restaurantvoting.TestData.USER_JWT;
import static ru.javaops.restaurantvoting.UserTestData.USER_ID;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.VoteAndLunchController.LUNCH_VOTING_URL;

public class VoteAndLunchControllerTest extends AbstractControllerTest {

    @Autowired
    private LunchService lunchService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private VoteAndLunchService voteAndLunchService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getLunchesOnDate() throws Exception {
        perform(MockMvcRequestBuilders
                .get(LUNCH_VOTING_URL, DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> LUNCH_WITH_VOTERS_MATCHER.matches(result, allLunchesOnDate));
    }

    @Test
    void batchCreateLunchesOnDate() throws Exception {
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL, NEW_DATE)
                .header(TestData.AUTHORIZATION_HEADER, ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(DISHES_FOR_MULTIPLE_LUNCHES)))
                .andDo(print())
                .andExpect(status().isOk());
        LUNCH_WITH_VOTERS_MATCHER.matches(voteAndLunchService.getLunchesForVoting(NEW_DATE), allBatchSavedLunches, "id", "voters");
    }


    @Test
    void vote() throws Exception {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL + "/vote", NEW_DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT)
                .param("restaurantId", String.valueOf(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(voteRepository.exists(NEW_DATE, USER_ID, BURGER_KING_ID));
    }

    @Test
    void voteAfterDeadline() throws Exception {
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL + "/vote", DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT)
                .param("restaurantId", String.valueOf(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isLocked());
    }

    @Test
    void voteForUnavailableLunch() throws Exception {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        lunchService.enable(BURGER_KING_ID, NEW_DATE, false);
        em.clear();
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL + "/vote", NEW_DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT)
                .param("restaurantId", String.valueOf(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void voteForUnavailableRestaurant() throws Exception {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        restaurantService.enable(BURGER_KING_ID, false);
        em.clear();
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL + "/vote", NEW_DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT)
                .param("restaurantId", String.valueOf(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void voteForDeletedRestaurant() throws Exception {
        lunchService.createOrUpdate(BURGER_KING_ID, NEW_DATE, dishIdsForNewLunch);
        restaurantService.softDelete(BURGER_KING_ID);
        em.clear();
        perform(MockMvcRequestBuilders
                .post(LUNCH_VOTING_URL + "/vote", NEW_DATE)
                .header(TestData.AUTHORIZATION_HEADER, USER_JWT)
                .param("restaurantId", String.valueOf(BURGER_KING_ID)))
                .andDo(print())
                .andExpect(status().isGone());
    }

}
