package ru.javaops.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.AbstractTest;
import ru.javaops.restaurantvoting.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.javaops.restaurantvoting.TestUtil.matches;
import static ru.javaops.restaurantvoting.UserTestData.*;

public class UserServiceTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void create() {
        matches(userService.create(newUserTo), newUser, "id", "registered", "password");
    }

    @Test
    void delete() {
        userService.delete(USER_ID);
        assertTrue(userRepository.findById(USER_ID).get().isDeleted());
    }
}
