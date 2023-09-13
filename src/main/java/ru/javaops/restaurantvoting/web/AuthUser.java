package ru.javaops.restaurantvoting.web;

import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.model.User;

@Getter
@Setter
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

}
