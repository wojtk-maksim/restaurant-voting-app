package ru.javaops.restaurantvoting.web;

import lombok.Getter;
import ru.javaops.restaurantvoting.model.User;

@Getter
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }
}
