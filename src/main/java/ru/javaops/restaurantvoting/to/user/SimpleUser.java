package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.javaops.restaurantvoting.to.AbstractNamedNewDataTo;

@Getter
@Setter
public class SimpleUser extends AbstractNamedNewDataTo {

    @NotBlank
    @Email
    @Size(max = 128)
    protected final String email;

    public SimpleUser(String name, String email) {
        super(name);
        this.email = email.toLowerCase();
    }

}
