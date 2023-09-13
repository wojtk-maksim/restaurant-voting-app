package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public abstract class AbstractNewUserTo {

    @NotBlank
    @Size(min = 3, max = 32)
    protected final String name;

    @NotBlank
    @Email
    @Size(max = 128)
    protected final String email;

    @NotBlank
    @Size(max = 32)
    protected final String newPassword;

    public AbstractNewUserTo(String name, String email, String newPassword) {
        this.name = name;
        this.email = email.toLowerCase();
        this.newPassword = newPassword;
    }

}
