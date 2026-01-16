// AuthTest.java
package ru.netology.ibank.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.RegistrationDto;
import ru.netology.ibank.data.UserGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

public class AuthTest {

    @BeforeAll
    static void setUpAll() {
        // Если сервер не присылает Content-Type, всё равно парсим как JSON
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void shouldLoginActiveUser() {
        // Генерация нового активного пользователя через API
        RegistrationDto activeUser = UserGenerator.generateActiveUser();

        // Попытка авторизации
        given()
                .baseUri("http://localhost")
                .port(9999)
                .contentType("application/json")
                .body(activeUser)
                .when()
                .post("/api/auth") // путь входа
                .then()
                .statusCode(200)
                .body("status", equalTo("ok"));
    }
}
