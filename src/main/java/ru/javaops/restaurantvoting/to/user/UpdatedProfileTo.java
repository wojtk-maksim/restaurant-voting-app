package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedProfileTo extends BasicProfileTo {

    @NotBlank(message = "{error.notBlank}")
    @Size(message = "{error.size}", min = 8, max = 128)
    private String passwordConfirmation;

    @Size(min = 8, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String newPassword;

    public UpdatedProfileTo(String name, String email, String passwordConfirmation, String newPassword) {
        super(null, name, email, null, null);
        this.passwordConfirmation = passwordConfirmation;
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "[name = '" + name + "', email = '" + email + "']";
    }

}
