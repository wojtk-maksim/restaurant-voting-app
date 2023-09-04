package ru.javaops.restaurantvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurantvoting.model.User;
import ru.javaops.restaurantvoting.model.Vote;
import ru.javaops.restaurantvoting.repository.UserRepository;
import ru.javaops.restaurantvoting.repository.VoteRepository;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurantvoting.TestUtil.extractJson;
import static ru.javaops.restaurantvoting.UserTestData.*;
import static ru.javaops.restaurantvoting.web.user.AdminUserController.ADMIN_USER_URL;

public class AdminUserControllerTest extends AbstractControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(List.of(admin, user), jsonUtil.readValues(extractJson(result), User[].class)));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_USER_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> assertEquals(user, jsonUtil.readValue(extractJson(result), User.class)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getNew())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    User created = jsonUtil.readValue(extractJson(result), User.class);
                    User newUser = getNew();
                    newUser.setId(created.getId());
                    assertEquals(newUser, created);
                });
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_USER_URL + "/" + USER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(jsonUtil.writeValue(getUpdated())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(result -> {
                    User updated = jsonUtil.readValue(extractJson(result), User.class);
                    assertEquals(getUpdated(), updated);
                });
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_USER_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userRepository.findById(USER_ID).isPresent());

        voteRepository.findById(new Vote.VoteId(LocalDate.of(2020, 9, 1), user));
    }
}
