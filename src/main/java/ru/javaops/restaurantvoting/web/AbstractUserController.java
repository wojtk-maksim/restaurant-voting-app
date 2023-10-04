package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.web.validation.UserValidator;

public abstract class AbstractUserController {

    @Autowired
    protected UserService userService;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

}
