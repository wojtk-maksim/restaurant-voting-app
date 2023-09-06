package ru.javaops.restaurantvoting.to.user.registered;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.restaurantvoting.model.Role;

import java.util.Date;
import java.util.Set;

@Value
@EqualsAndHashCode
public class UserProfileTo {

    Integer id;

    String name;

    String email;

    Date registered;

    Set<Role> roles;

    boolean enabled;
}
