package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.restaurantvoting.util.validation.MatchPassword;

@Value
@EqualsAndHashCode(callSuper = true)
public class UpdatedUserTo extends AbstractUserTo {

    @NotBlank
    @Size(max = 32)
    @MatchPassword
    String oldPassword;

    @Size(max = 32)
    String password;

    public UpdatedUserTo(String name, String email, String oldPassword, String password) {
        super(name, email);
        this.oldPassword = oldPassword;
        this.password = password;
    }
}
