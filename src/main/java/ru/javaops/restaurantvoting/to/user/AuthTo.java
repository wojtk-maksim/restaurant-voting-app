package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
// Can not use ProfileTo for controller's authentication method.
// As spring validator will validate it and reject email as already registered.
public class AuthTo extends BasicUserTo {

    @NotBlank(message = "{error.notBlank}")
    @Size(message = "{error.size}", min = 8, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;

    @Override
    public String toString() {
        return "[email = " + email + "]";
    }

}
