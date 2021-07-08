package com.kma.practice8.springsecuritycustom.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;

public class MyCustomUserFactory implements WithSecurityContextFactory<MyCustomUser> {

    @Override
    public SecurityContext createSecurityContext(final MyCustomUser annotation) {
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(
            "login",
            "password",
            Arrays.stream(annotation.permissions()).collect(Collectors.toList()),
            annotation.companyAlias()
        );

        final UsernamePasswordAuthenticationToken auth
            = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        return securityContext;
    }
}
