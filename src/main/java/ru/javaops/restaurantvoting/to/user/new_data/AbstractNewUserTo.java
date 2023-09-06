package ru.javaops.restaurantvoting.to.user.new_data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class AbstractNewUserTo {

    @NotBlank
    @Size(min = 3, max = 32)
    String name;

    @NotBlank
    @Email
    @Size(max = 128)
    String email;
}
