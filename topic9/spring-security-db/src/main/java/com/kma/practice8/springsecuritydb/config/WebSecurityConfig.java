package com.kma.practice8.springsecuritydb.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.kma.practice8.springsecuritydb.domain.type.Permission;
import com.kma.practice8.springsecuritydb.repositories.UserRepository;
import com.kma.practice8.springsecuritydb.service.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/admin", "/admin/**").hasAuthority(Permission.VIEW_ADMIN.name())
            .antMatchers("/catalog").hasAuthority(Permission.VIEW_CATALOG.name())
            .antMatchers("/profile").authenticated()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling().authenticationEntryPoint((request, response, e) -> {
                response.setStatus(HttpServletResponse. SC_UNAUTHORIZED);
            })
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService(userRepository);
    }
}
