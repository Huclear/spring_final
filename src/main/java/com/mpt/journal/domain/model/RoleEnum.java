package com.mpt.journal.domain.model;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    USER, ADMIN, EDITOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
