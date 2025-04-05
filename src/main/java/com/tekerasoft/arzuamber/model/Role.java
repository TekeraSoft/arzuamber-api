package com.tekerasoft.arzuamber.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
