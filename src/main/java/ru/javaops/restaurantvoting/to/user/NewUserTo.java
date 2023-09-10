package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class NewUserTo extends AbstractUserTo {

    @NotBlank
    @Size(max = 32)
    String password;

    public NewUserTo(String name, String email, String password) {
        super(name, email);
        this.password = password;
    }
}
