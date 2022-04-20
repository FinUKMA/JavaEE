package com.kma.practice8.springsecuritydb.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
public class MyCustomUser extends User {

    @Getter
    private final Integer companyId;

    public MyCustomUser(final String username, final String password, final Collection<? extends GrantedAuthority> authorities, final Integer companyId) {
        super(username, password, authorities);
        this.companyId = companyId;
    }
}
