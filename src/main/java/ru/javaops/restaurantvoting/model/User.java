package ru.javaops.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User extends NamedEntity implements Enablable, Deletable {

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    @JsonView(Public.class)
    protected boolean enabled = true;
    @Column(name = "email", nullable = false, unique = true)
    @JsonView(Public.class)
    private String email;
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @JsonView(Public.class)
    private Date registered = new Date();
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

    public User(Long id, String name, String email, String password, Set<Role> roles, boolean enabled, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
        this.deleted = deleted;
    }

    public User(Long id, String name, String email, String password, Set<Role> roles, boolean enabled) {
        this(id, name, email, password, roles, enabled, false);
    }

    public User(Long id, String name, String email, String password, Set<Role> roles) {
        this(id, name, email, password, roles, true);
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, EnumSet.of(Role.USER));
    }

    @Override
    public String toString() {
        return "User {id: " + id + ", name: " + name + ", email: " + email + ", password: " + password + "}";
    }

}
