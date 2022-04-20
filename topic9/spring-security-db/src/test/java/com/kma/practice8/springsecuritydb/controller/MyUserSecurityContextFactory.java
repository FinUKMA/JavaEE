package com.kma.practice8.springsecuritydb.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.kma.practice8.springsecuritydb.domain.type.Permission;
import com.kma.practice8.springsecuritydb.service.MyCustomUser;

public class MyUserSecurityContextFactory implements WithSecurityContextFactory<MyMockUser> {

    @Override
    public SecurityContext createSecurityContext(final MyMockUser annotation) {
        MyCustomUser principal = new MyCustomUser(
            annotation.username(), "password", mapAuthorities(annotation.authorities()), annotation.companyId() < 0 ? null : annotation.companyId()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    private static List<GrantedAuthority> mapAuthorities(final Permission[] permissions) {
        return Arrays.stream(permissions)
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableList());
    }
}
