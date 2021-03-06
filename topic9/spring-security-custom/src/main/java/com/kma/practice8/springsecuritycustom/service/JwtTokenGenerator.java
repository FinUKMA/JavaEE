package com.kma.practice8.springsecuritycustom.service;

import java.security.Key;

import org.springframework.stereotype.Component;

import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenGenerator {

    private static final Key TOKEN_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(final AuthenticatedUser authenticatedUser) {
        return Jwts.builder()
            .setSubject(authenticatedUser.getUsername())
            .signWith(TOKEN_KEY)
            .compact();
    }

    public String getUsernameFromToken(final String jwt) {
        return Jwts.parserBuilder()
            .setSigningKey(TOKEN_KEY)
            .build()
            .parseClaimsJws(jwt)
            .getBody()
            .getSubject();
    }

}
