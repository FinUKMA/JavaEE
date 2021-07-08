package com.kma.practice8.springsecuritycustom.domain.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import com.kma.practice8.springsecuritycustom.domain.type.Permission;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class AuthenticatedUser extends User {

    private final String companyAlias;

    public AuthenticatedUser(
        final String username,
        final String password,
        final List<Permission> authorities,
        final String companyAlias
    ) {
        super(username, password, authorities);
        this.companyAlias = companyAlias;
    }
}
