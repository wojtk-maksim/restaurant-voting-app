package ru.javaops.restaurantvoting.error.authentication;

import org.springframework.security.core.AuthenticationException;
import ru.javaops.restaurantvoting.error.HasUri;

public class NotFoundAuthenticationException extends AuthenticationException implements HasUri {

    public NotFoundAuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/auth-not-found";
    }

}
