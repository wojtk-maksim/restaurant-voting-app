package ru.javaops.restaurantvoting.to.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.model.Role;
import ru.javaops.restaurantvoting.to.BasicEnablabeDeletableTo;

import static ru.javaops.restaurantvoting.model.Role.BANNED;
import static ru.javaops.restaurantvoting.model.Role.DELETED;
import static ru.javaops.restaurantvoting.service.UserService.DELETED_USER_NAME;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "role"})
@JsonIgnoreProperties({"enabled", "deleted", "lunchId"})
public class VoterTo extends BasicEnablabeDeletableTo {

    private Role role;

    private Long lunchId;

    // Modify banned and deleted users right in the constructor. For performance’s sake.
    public VoterTo(Long id, String name, Role role, boolean enabled, boolean deleted, Long lunchId) {
        super(id, name, enabled, deleted);
        if (deleted) {
            this.name = DELETED_USER_NAME;
            this.role = DELETED;
        } else {
            this.name = name;
            this.role = enabled ? role : BANNED;
        }
        this.lunchId = lunchId;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", name = '" + name + "']";
    }

}
