package kma.topic8.springrestsample;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kma.topic8.springrestsample.dto.LoginResponseDto;

@WebMvcTest({
    LoginController.class
})
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldSendRequest() throws Exception {
        when(userService.doLogin(any()))
            .thenReturn(LoginResponseDto.of("login1", "message2"));

        mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
                .queryParam("requiredField", "asd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(LoginControllerTest.class.getResourceAsStream("/request.json").readAllBytes())
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(
                new String(LoginControllerTest.class.getResourceAsStream("/response.json").readAllBytes())
            ))
            .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, "generated-jwt-token"));
    }

    @Test
    void shouldHandleMissingParameter() throws Exception {
        when(userService.doLogin(any()))
            .thenReturn(LoginResponseDto.of("login1", "message2"));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(LoginControllerTest.class.getResourceAsStream("/request.json").readAllBytes())
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().json(
                new String(LoginControllerTest.class.getResourceAsStream("/errorMissingParameter.json").readAllBytes())
            ))
            .andExpect(MockMvcResultMatchers.header().doesNotExist(HttpHeaders.AUTHORIZATION));
    }

}
