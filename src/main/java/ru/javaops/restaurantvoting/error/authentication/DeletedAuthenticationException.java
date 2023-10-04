package ru.javaops.restaurantvoting.error.authentication;

import org.springframework.security.core.AuthenticationException;
import ru.javaops.restaurantvoting.error.HasUri;

public class DeletedAuthenticationException extends AuthenticationException implements HasUri {

    public DeletedAuthenticationException(String msg) {
        super(msg);
    }

    @Override
    public String getProblemPath() {
        return "/auth-deleted";
    }

}
