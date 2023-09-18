package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.javaops.restaurantvoting.util.Views.Public;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends NamedDeletableEntity {

    @Column(name = "email", nullable = false, unique = true)
    @JsonView(Public.class)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @JsonView(Public.class)
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JsonView(Public.class)
    private Role role = Role.USER;

    public User(Long id, String name, String email, String password, Role role, boolean enabled, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.deleted = deleted;
    }

    public User(Long id, String name, String email, String password, Role role, boolean enabled) {
        this(id, name, email, password, role, enabled, false);
    }

    public User(Long id, String name, String email, String password, Role role) {
        this(id, name, email, password, role, true);
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, Role.USER);
    }

    @Override
    public String toString() {
        return "User {id: " + id + ", name: " + name + ", email: " + email + "}";
    }

}
