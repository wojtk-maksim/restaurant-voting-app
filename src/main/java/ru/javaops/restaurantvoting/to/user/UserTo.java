package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import ru.javaops.restaurantvoting.model.Deletable;
import ru.javaops.restaurantvoting.model.Enablable;
import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.to.AbstractNamedTo;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.util.Views.Public;

import java.util.Date;
import java.util.Set;

@Getter
@JsonPropertyOrder({"id", "name", "email", "registered", "roles", "enabled", "deleted"})
public class UserTo extends AbstractNamedTo implements Enablable, Deletable {

    @JsonView(Public.class)
    private final String email;

    @JsonView(Public.class)
    private final Date registered;

    @JsonView(Public.class)
    private final Set<Role> roles;

    private final boolean enabled;

    @JsonView(Admin.class)
    private final boolean deleted;

    public UserTo(Long id, String name, String email, Date registered, Set<Role> roles, boolean enabled, boolean deleted) {
        super(id, name);
        this.email = email;
        this.registered = registered;
        this.roles = roles;
        this.enabled = enabled;
        this.deleted = deleted;
    }

}
