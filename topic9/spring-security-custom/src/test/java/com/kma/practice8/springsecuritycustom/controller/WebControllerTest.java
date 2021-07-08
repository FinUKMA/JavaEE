package com.kma.practice8.springsecuritycustom.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
            get("/admin")
        )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @MyCustomMockUser(permissions = Permission.VIEW_ADMIN)
    //@WithMockUser(authorities = "VIEW_ADMIN")
    void test2() {
        mockMvc.perform(
            get("/admin")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("admin_string"));
    }

    @Test
    @SneakyThrows
    //@WithMockUser//(authorities = "VIEW_ADMIN")
    @MyCustomMockUser(companyAlias = "alias")
    void test3() {
        mockMvc.perform(
            get("/company/alias")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("asd"));
    }

    @Test
    @SneakyThrows
    //@WithMockUser//(authorities = "VIEW_ADMIN")
    @MyCustomMockUser(companyAlias = "alias1")
    void test4() {
        mockMvc.perform(
            get("/company/alias1")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("asd"));
    }
}