package ru.javaops.restaurantvoting.error.authentication;

import org.springframework.security.core.AuthenticationException;
import ru.javaops.restaurantvoting.error.HasUri;

public class ExpiredAuthenticationException extends AuthenticationException implements HasUri {

    public ExpiredAuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/auth-expired";
    }

}
