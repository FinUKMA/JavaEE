package kma.topic8.springrestsample;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kma.topic8.springrestsample.dto.LoginDto;
import kma.topic8.springrestsample.dto.LoginResponseDto;
import lombok.SneakyThrows;

@WebMvcTest(LoginController.class)
class MyRestControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void shouldHandleLoginRequest() {
        when(userService.doLogin(any())).thenReturn(LoginResponseDto.of("login-value", "success-message"));

        mockMvc.perform(
            post("/login")
                .queryParam("requiredField", "value")
                .content(MyRestControllerTest.class.getResourceAsStream("/request.json").readAllBytes())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.AUTHORIZATION, "generated-jwt-token"))
            .andExpect(content().json(new String(MyRestControllerTest.class.getResourceAsStream("/response.json").readAllBytes())));

        verify(userService).doLogin(new LoginDto("username", "secured-password"));
    }


    @Test
    @SneakyThrows
    void shouldHandleLoginRequest_withObjectMapper() {
        final LoginDto request = new LoginDto("username", "secured-password");
        final LoginResponseDto response = LoginResponseDto.of("username", "success message");
        when(userService.doLogin(any())).thenReturn(response);

        mockMvc.perform(
            post("/login")
                .queryParam("requiredField", "value")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.AUTHORIZATION, "generated-jwt-token"))
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(userService).doLogin(request);
    }

    @Test
    @SneakyThrows
    void shouldReturnException_whenMissingRequestParam() {
        mockMvc.perform(
            post("/login")
                .content(objectMapper.writeValueAsString(new LoginDto("username", "secured-password")))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
            .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
            .andExpect(content().string("Required String parameter 'requiredField' is not present"));

        verifyNoInteractions(userService);
        // alternative
        verify(userService, never()).doLogin(any());
    }

}
