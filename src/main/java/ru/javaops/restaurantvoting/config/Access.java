package ru.javaops.restaurantvoting.config;

import org.springframework.security.core.GrantedAuthority;

public enum Access implements GrantedAuthority {

    PUBLIC_ACCESS,
    ADMIN_ACCESS,
    SUPER_ADMIN_ACCESS;

    @Override
    public String getAuthority() {
        return name();
    }

}
