package ru.javaops.restaurantvoting.to.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class AbstractUserTo {

    @NotBlank
    @Size(min = 3, max = 32)
    protected String name;

    @NotBlank
    @Email
    @Size(max = 128)
    protected String email;
}
