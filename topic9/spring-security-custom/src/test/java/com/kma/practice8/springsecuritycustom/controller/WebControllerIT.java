package com.kma.practice8.springsecuritycustom.controller;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import lombok.SneakyThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebControllerIT {

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }


    @Test
    @SneakyThrows
    void test1_admin() {
        final String token = getToken("admin", "password");
        System.out.println(token);

        given()
            .header(HttpHeaders.AUTHORIZATION, token)
        .when()
            .get("/admin")
        .then()
            .statusCode(200);
    }

    @Test
    @SneakyThrows
    void test1_user() {
        final String token = getToken("user", "password");
        System.out.println(token);

        given()
            .header(HttpHeaders.AUTHORIZATION, token)
        .when()
            .get("/admin")
        .then()
            .statusCode(403);
    }

    private static String getToken(final String login, final String password) {
        return given()
            .body(Map.of(
                "login", login,
                "password", password
            ))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .when()
            .post("/my-login")
        .then()
            .statusCode(200)
            .extract()
            .header(HttpHeaders.AUTHORIZATION);
    }
}