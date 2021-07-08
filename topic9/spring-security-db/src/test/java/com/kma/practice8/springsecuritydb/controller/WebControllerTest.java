package com.kma.practice8.springsecuritydb.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
            MockMvcRequestBuilders.get("/admin")
        )
            .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = "VIEW_ADMIN")
    void test2() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/admin")
        )
            .andExpect(status().isOk())
            .andExpect(content().string("admin_root"));
    }

}