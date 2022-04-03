package kma.topic8.springrestsample;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kma.topic8.springrestsample.dto.LoginResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerIT {

    @LocalServerPort
    void initTest(int port) {
        RestAssured.port = port;
    }

    @Test
    void shouldSendRequest() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .content(LoginControllerIT.class.getResourceAsStream("/request.json"))
                .queryParam("requiredField", "test1")
            .when()
                .post("/login")
            .then()
                .statusCode(200)
                .body("login", CoreMatchers.is("myLogin"))
                .body("successMessage", CoreMatchers.is("success"));
    }

}
