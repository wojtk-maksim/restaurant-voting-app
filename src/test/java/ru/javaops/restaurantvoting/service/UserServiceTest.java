package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.UserTestData.*;

public class UserServiceTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void getAll() {
        USER_MATCHER.matches(userService.getAll(), users);
    }

    @Test
    void get() {
        USER_MATCHER.matches(userService.get(USER_ID), user);
    }

    @Test
    void register() {
        USER_MATCHER.matches(userService.register(NEW_PROFILE), newUser, "id");
    }

    @Test
    void updateProfile() {
        USER_MATCHER.matches(userService.updateAccount(getUser(), UPDATED_PROFILE), updatedUser);
    }

    @Test
    void delete() {
        userService.delete(USER_ID);
        assertTrue(userRepository.get(USER_ID).isDeleted());
    }

}
