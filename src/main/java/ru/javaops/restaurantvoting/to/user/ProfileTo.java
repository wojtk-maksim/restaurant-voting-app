package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.javaops.restaurantvoting.model.Role;

import java.util.Date;

import static ru.javaops.restaurantvoting.model.Role.USER;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "email", "registered", "role"})
public class ProfileTo extends BasicProfileTo {

    @NotBlank(message = "{error.notBlank}")
    @Size(message = "{error.size}", min = 8, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;

    public ProfileTo(Long id, String name, String email, String password, Date registered, Role role) {
        super(id, name, email, registered, role == null ? USER : role);
        this.password = password;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
