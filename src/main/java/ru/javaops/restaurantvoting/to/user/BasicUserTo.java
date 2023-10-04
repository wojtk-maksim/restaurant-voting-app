package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.Views.Public;
import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.web.validation.NoHtml;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicUserTo {

    @JsonView(Public.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @NotBlank(message = "{error.notBlank}")
    @Email(message = "{error.emailFormat}")
    @Size(message = "{error.size}", min = 5, max = 128)
    @NoHtml
    protected String email;

    @JsonView(Public.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Role role;

    @Override
    public String toString() {
        return "[email = '" + email + "']";
    }

}
