package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurantvoting.util.Views.Admin;
import ru.javaops.restaurantvoting.util.Views.Public;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends NamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @JsonView(Public.class)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Public.class)
    private Date registered = new Date();

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Public.class)
    private boolean enabled = true;

    @Column(name = "deleted", nullable = false, columnDefinition = "bool default false")
    @JsonView(Admin.class)
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "uk_user_role")
    )
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonView(Public.class)
    private Set<Role> roles;

    public User(Long id, String name, String email, String password, Date registered, boolean enabled, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(Long id, String name, String email, String password, boolean enabled, Set<Role> roles) {
        this(id, name, email, password, new Date(), enabled, roles);
    }

    public User(Long id, String name, String email, String password, Set<Role> roles) {
        this(id, name, email, password, true, roles);
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, EnumSet.of(Role.USER));
    }

    @Override
    public String toString() {
        return "User {id: " + id + ", name: " + name + ", email: " + email + ", password: " + password + "}";
    }
}
