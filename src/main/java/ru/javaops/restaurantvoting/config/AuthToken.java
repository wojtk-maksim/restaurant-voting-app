package ru.javaops.restaurantvoting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.javaops.restaurantvoting.HasIdAndEmail;
import ru.javaops.restaurantvoting.model.Role;

@Getter
@Setter
public class AuthToken extends AbstractAuthenticationToken implements HasIdAndEmail {

    private Long id;

    private String name;

    private String email;

    private Role role;

    public AuthToken(Long id, String name, String email, Role role) {
        super(role.getAuthorities());
        super.setAuthenticated(true);
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public String toString() {
        return "[id = " + id + ", name ='" + name + "', email = '" + email + "']";
    }

}
