package com.kma.practice8.springsecuritydb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.kma.practice8.springsecuritydb.repositories.UserRepository;

import lombok.SneakyThrows;

@WebMvcTest
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    @WithMockUser(authorities = "VIEW_ADMIN")
    void shouldAccessAdminPage() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/admin")
        )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = "VIEW_CATALOG")
    void shouldAccessAdminPage_withCatalogPermission() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin")
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void shouldReturnForbidden_whenNoAdminPermission() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin")
            )
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @SneakyThrows
    void shouldRedirectToLogin_whenNoUser() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin")
            )
            .andExpect(MockMvcResultMatchers.status().isFound())
            .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/login"));
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = "VIEW_ADMIN")
    void shouldAccessCompanyEditPage_admin() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/company/10/edit")
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @MyMockUser(companyId = 10)
    void shouldAccessCompanyEditPage_notAdmin() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/company/10/edit")
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @MyMockUser(companyId = 20)
    void shouldAccessCompanyEditPage_notAdminAndDifferentCompanyId() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/company/10/edit")
            )
            .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
