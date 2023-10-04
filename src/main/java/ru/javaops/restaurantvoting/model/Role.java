package ru.javaops.restaurantvoting.model;

import lombok.Getter;
import ru.javaops.restaurantvoting.config.Access;

import java.util.EnumSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static ru.javaops.restaurantvoting.config.Access.*;

@Getter
public enum Role {

    USER(EnumSet.of(PUBLIC_ACCESS)),
    ADMIN(EnumSet.of(PUBLIC_ACCESS, ADMIN_ACCESS)),
    SUPER_ADMIN(EnumSet.of(PUBLIC_ACCESS, ADMIN_ACCESS, SUPER_ADMIN_ACCESS)),
    BANNED(emptySet()),
    DELETED(emptySet());


    private final Set<Access> authorities;

    Role(Set<Access> authorities) {
        this.authorities = authorities;
    }

}
