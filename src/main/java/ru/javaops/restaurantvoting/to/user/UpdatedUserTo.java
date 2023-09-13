package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ru.javaops.restaurantvoting.util.validation.user.MatchPassword;

@Getter
public class UpdatedUserTo extends AbstractNewUserTo {

    @NotBlank
    @Size(max = 32)
    @MatchPassword
    String oldPassword;

    public UpdatedUserTo(String name, String email, String newPassword, String oldPassword) {
        super(name, email, newPassword);
        this.oldPassword = oldPassword;
    }

}
