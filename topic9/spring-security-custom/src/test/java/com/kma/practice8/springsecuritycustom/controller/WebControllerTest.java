package com.kma.practice8.springsecuritycustom.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kma.practice8.springsecuritycustom.controller.WebController;
import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;
import com.kma.practice8.springsecuritycustom.domain.type.Permission;
import com.kma.practice8.springsecuritycustom.repositories.UserRepository;
import com.kma.practice8.springsecuritycustom.service.JwtTokenGenerator;

import lombok.SneakyThrows;

@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtTokenGenerator jwtTokenGenerator;

    @Test
    @SneakyThrows
    void test1() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/admin")
        )
            .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @MyCustomUser(permissions = Permission.VIEW_ADMIN)
    void test01() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/admin")
        )
            .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @MyCustomUser(companyAlias = "asd")
    void test2() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/company/asd")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("asd"));
    }

    @Test
    @SneakyThrows
    @MyCustomUser(companyAlias = "asd1")
    void test3() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/company/asd1")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("asd"));
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    @SneakyThrows
    void test4(final AuthenticatedUser authenticatedUser) {
        final UsernamePasswordAuthenticationToken auth
            = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/company/asd")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("asd"));

        SecurityContextHolder.clearContext();
    }

    private static Stream<AuthenticatedUser> dataProvider() {
        return Stream.of(
            new AuthenticatedUser("login", "password", List.of(Permission.VIEW_ADMIN), null),
            new AuthenticatedUser("login", "password", List.of(), "asd")
        );
    }

}