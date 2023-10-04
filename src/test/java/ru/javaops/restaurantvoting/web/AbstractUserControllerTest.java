package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.restaurantvoting.service.UserService;

public abstract class AbstractUserControllerTest extends AbstractControllerTest {

    @Autowired
    protected UserService userService;
}
