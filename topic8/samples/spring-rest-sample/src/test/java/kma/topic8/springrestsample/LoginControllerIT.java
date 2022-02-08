package kma.topic8.springrestsample;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kma.topic8.springrestsample.dto.LoginResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerIT {

    @MockBean
    private UserService userService;

    @LocalServerPort
    void savePort(int port) {
        RestAssured.port = port;
    }

    @Test
    void shouldSendRequest() {
        Mockito.when(userService.doLogin(ArgumentMatchers.any()))
                .thenReturn(LoginResponseDto.of("other-login", "message"));

        RestAssured
            .given()
                .queryParam("requiredField", "stejwhewhrge")
                .contentType(ContentType.JSON)
                .body(LoginControllerIT.class.getResourceAsStream("/request.json"))
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .body("login", CoreMatchers.is("other-login"))
                .body("message", CoreMatchers.is("message"));
    }

}
