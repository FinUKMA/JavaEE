package com.kma.practice8.springsecuritycustom.controller;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kma.practice8.springsecuritycustom.domain.security.AuthenticatedUser;
import com.kma.practice8.springsecuritycustom.domain.type.Permission;
import com.kma.practice8.springsecuritycustom.repositories.UserRepository;
import com.kma.practice8.springsecuritycustom.service.JwtTokenGenerator;

import io.restassured.RestAssured;
import lombok.SneakyThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebControllerIT {

    @LocalServerPort
    void savePort(final int port) {
        RestAssured.port = port;
    }

    @Test
    void test1() {
        final String token = getToken("admin", "password");
        System.out.println(token);

        given()
            .header(HttpHeaders.AUTHORIZATION, token)
        .when()
            .get("/admin")
        .then()
            .statusCode(200);
    }

    private static String getToken(final String login, final String password) {
        return given()
            .body(Map.of(
                "login", login,
                "password", password
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/my-login")
        .then()
            .statusCode(200)
            .extract()
            .header(HttpHeaders.AUTHORIZATION);
    }

}