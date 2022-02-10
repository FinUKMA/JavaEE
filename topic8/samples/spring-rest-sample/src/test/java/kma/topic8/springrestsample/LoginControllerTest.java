package kma.topic8.springrestsample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kma.topic8.springrestsample.dto.LoginResponseDto;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldSendRequest() throws Exception {
        String expectedResponse = new String(LoginControllerTest.class.getResourceAsStream("/expectedResponse.json").readAllBytes());

        Mockito.when(userService.doLogin(ArgumentMatchers.any()))
                .thenReturn(LoginResponseDto.of("myLogin", "success message"));

        mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(LoginControllerTest.class.getResourceAsStream("/request.json").readAllBytes())
                .queryParam("requiredField", "awegweg")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, "generated-jwt-token"))
            .andExpect(MockMvcResultMatchers.content().json(expectedResponse));

        Mockito.verify(userService).doLogin(ArgumentMatchers.any());
    }

    @Test
    void shouldHandleMissingParam() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(LoginControllerTest.class.getResourceAsStream("/request.json").readAllBytes())
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.header().doesNotExist(HttpHeaders.AUTHORIZATION))
            .andExpect(MockMvcResultMatchers.content().string("Required String parameter 'requiredField' is not present"));

        Mockito.verifyNoInteractions(userService);
    }

}
