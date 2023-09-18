package ru.javaops.restaurantvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.restaurantvoting.service.UserService;
import ru.javaops.restaurantvoting.util.EmailAndPasswordValidator;

public abstract class AbstractUserController {

    @Autowired
    protected UserService userService;

    @Autowired
    private EmailAndPasswordValidator emailAndPasswordValidator;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(emailAndPasswordValidator);
    }

}
