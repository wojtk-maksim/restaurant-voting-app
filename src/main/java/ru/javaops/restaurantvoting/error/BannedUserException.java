package ru.javaops.restaurantvoting.error;

import org.springframework.security.core.AuthenticationException;
import ru.javaops.restaurantvoting.model.User;

public class BannedUserException extends AuthenticationException {

    public BannedUserException(User user) {
        super(user + " is banned");
    }

}
