package com.kma.practice8.springsecuritycustom.domain.type;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {

    VIEW_ADMIN,
    VIEW_CATALOG;

    @Override
    public String getAuthority() {
        return name();
    }
}
