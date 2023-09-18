package ru.javaops.restaurantvoting.to.user;

import lombok.Getter;

@Getter
public class NewAccount extends SimpleUser {

    private final String password;

    public NewAccount(String name, String email, String password) {
        super(name, email);
        this.password = password;
    }

}
