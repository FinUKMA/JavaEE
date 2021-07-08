package com.kma.practice8.springsecuritydb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.kma.practice8.springsecuritydb.repositories.UserRepository;

import lombok.SneakyThrows;

@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    void test1() {
        mockMvc.perform(
            get("/admin")
        )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = "VIEW_ADMIN")
    void test2() {
        mockMvc.perform(
            get("/admin")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("admin_root"));
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("dataProvider")
    void test3(User user) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        mockMvc.perform(
            get("/profile")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("profile"));

        SecurityContextHolder.clearContext();
    }

    private static Stream<User> dataProvider() {
        return Stream.of(
            new User("login", "password", List.of()),
            new User("login", "password", List.of(new SimpleGrantedAuthority("VIEW_ADMIN")))
        );
    }

}