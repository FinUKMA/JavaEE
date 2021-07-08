package com.kma.practice8.springsecuritycustom.controller;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;

public class MyCustomMockUserFactory implements WithSecurityContextFactory<MyCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(final MyCustomMockUser annotation) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
            "login",
            "password",
            Arrays.asList(annotation.permissions()),
            annotation.companyAlias()
        );

        UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
