package ru.javaops.restaurantvoting.web.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.to.user.registered.UserProfileTo;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.TestUtil.parseObject;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.user.ProfileController.PROFILE_URL;

public class ProfileControllerTest extends AbstractUserControllerTest {

    @Autowired
    private EntityManager em;

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROFILE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, UserProfileTo.class), getUserTo(), "registered"));
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PROFILE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(getNewUserTo())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> matches(parseObject(result, UserProfileTo.class), getNewUserProfileTo(), "id", "registered"));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PROFILE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(getUpdatedUserTo())))
                .andDo(print())
                .andExpect(status().isOk());
        em.clear();
        matches(userService.get(USER_ID), getUpdatedUserProfileTo(), "registered");
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PROFILE_URL))
                .andDo(print())
                .andExpect(status().isOk());
        em.clear();
        matches(userService.get(USER_ID), getDeletedUserProfileTo(), "registered");
    }
}
