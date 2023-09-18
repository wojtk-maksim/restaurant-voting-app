package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatedAccount extends SimpleUser {

    @NotBlank
    @Size(max = 32)
    private final String currentPassword;

    private final String newPassword;

    public UpdatedAccount(String name, String email, String currentPassword, String newPassword) {
        super(name, email);
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

}
