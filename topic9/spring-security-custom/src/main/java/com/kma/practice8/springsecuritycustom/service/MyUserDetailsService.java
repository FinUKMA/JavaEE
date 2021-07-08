package com.kma.practice8.springsecuritycustom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kma.practice8.springsecuritycustom.domain.entities.PermissionEntity;
import com.kma.practice8.springsecuritycustom.domain.entities.UserEntity;
import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;
import com.kma.practice8.springsecuritycustom.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity user = userRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user with login: " + username));

        return new AuthenticatedUser(
            username,
            user.getPassword(),
            user.getRole().getPermissions().stream().map(PermissionEntity::getPermission).collect(Collectors.toList()),
            user.getCompany()
        );
    }
}
