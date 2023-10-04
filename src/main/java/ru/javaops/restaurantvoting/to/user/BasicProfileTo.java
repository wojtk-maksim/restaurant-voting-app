package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.javaops.restaurantvoting.Views;
import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.web.validation.NoHtml;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicProfileTo extends BasicUserTo {

    @NotBlank(message = "{error.notBlank}")
    @Size(message = "{error.size}", min = 4, max = 32)
    @NoHtml
    @JsonView(Views.Public.class)
    protected String name;

    @JsonView(Views.Public.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Date registered;

    public BasicProfileTo(Long id, String name, String email, Date registered, Role role) {
        super(id, email, role);
        this.name = name;
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "', email = '" + email + "']";
    }

}
