package ru.javaops.restaurantvoting.web.public_access;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.config.AuthToken;
import ru.javaops.restaurantvoting.web.AbstractUserControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestData.*;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.model.Role.DELETED;
import static ru.javaops.restaurantvoting.util.JsonUtil.writeValue;
import static ru.javaops.restaurantvoting.util.JwtUtil.getUserAuthenticationToken;
import static ru.javaops.restaurantvoting.web.public_access.ProfileController.PROFILE_URL;

public class ProfileControllerTest extends AbstractUserControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, user));
    }

    @Test
    void getBanned() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, BANNED_USER_JWT))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getDeleted() throws Exception {
        perform(MockMvcRequestBuilders.get(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, DELETED_USER_JWT))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        perform(MockMvcRequestBuilders.post(PROFILE_URL + "/register")
                .contentType(APPLICATION_JSON)
                .content(writeValue(NEW_USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> USER_MATCHER.matches(result, newUser, "id"));
    }

    @Test
    void registerEmailAndNameExists() throws Exception {
        perform(MockMvcRequestBuilders.post(PROFILE_URL + "/register")
                .contentType(APPLICATION_JSON)
                .content(writeValue(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void authenticate() throws Exception {
        perform(MockMvcRequestBuilders.post(PROFILE_URL + "/auth")
                .contentType(APPLICATION_JSON)
                .content(writeValue(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    AuthToken authToken = getUserAuthenticationToken(result.getResponse().getContentAsString());
                    assertTrue(USER_ID.equals(authToken.getId()) && USER_EMAIL.equals(authToken.getEmail()));
                });
    }

    @Test
    void update() throws Exception {
        perform(MockMvcRequestBuilders.put(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, USER_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(UPDATED_PROFILE)))
                .andDo(print())
                .andExpect(status().isOk());
        PROFILE_TO_MATCHER.matches(userService.get(USER_ID), UPDATED_USER);
    }

    @Test
    void updateInvalidPasswordConfirmation() throws Exception {
        perform(MockMvcRequestBuilders.put(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, USER_JWT)
                .contentType(APPLICATION_JSON)
                .content(writeValue(invalidPasswordConfirmationUserTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders
                .delete(PROFILE_URL)
                .header(AUTHORIZATION_HEADER, USER_JWT))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(DELETED, userService.get(USER_ID).getRole());
    }

}
