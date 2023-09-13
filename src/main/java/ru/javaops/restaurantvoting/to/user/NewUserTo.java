package ru.javaops.restaurantvoting.to.user;

import lombok.Getter;

@Getter
public class NewUserTo extends AbstractNewUserTo {

    public NewUserTo(String name, String email, String newPassword) {
        super(name, email, newPassword);
    }

}
