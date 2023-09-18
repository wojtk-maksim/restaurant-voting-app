package ru.javaops.restaurantvoting.web.public_access;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.User;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.web.public_access.AccountController.ACCOUNT_URL;

public class AccountControllerTest extends AbstractUserControllerTest {

    @Autowired
    private EntityManager em;

    @Test
    @WithUserDetails(USER_EMAIL)
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, user, User.class));
    }

    @Test
    @WithUserDetails(BANNED_USER_EMAIL)
    void getBanned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ACCOUNT_URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ACCOUNT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(NEW_PROFILE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, newUser, User.class, "id"));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ACCOUNT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(UPDATED_PROFILE)))
                .andDo(print())
                .andExpect(status().isOk());
        USER_MATCHER.matches(userService.get(USER_ID), updatedUser);
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void updateInvalidPasswordConfirmation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ACCOUNT_URL)
                        .contentType(APPLICATION_JSON)
                        .content(writeValue(invalidPasswordConfirmationUserTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ACCOUNT_URL))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(userService.get(USER_ID).isDeleted());
    }

}
