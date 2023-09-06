package ru.javaops.restaurantvoting.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.web.AbstractControllerTest;

public abstract class AbstractUserControllerTest extends AbstractControllerTest {

    @Autowired
    protected UserService userService;
}
