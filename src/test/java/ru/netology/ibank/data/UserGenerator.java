// UserGenerator.java
package ru.netology.ibank.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("ru"));

    private UserGenerator() {
    } // запрет создания экземпляров

    // Основной метод генерации пользователя
    public static RegistrationDto generateUser(String status) {
        String login = faker.name().username().replace("ё", "е") + faker.number().digits(3);
        String password = faker.internet().password(8, 12);

        RegistrationDto user = new RegistrationDto(login, password, status);

        // Отправка пользователя в тестовый сервис
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return user;
    }

    public static RegistrationDto activeUser() {
        return generateUser("active");
    }

    public static RegistrationDto blockedUser() {
        return generateUser("blocked");
    }
}
