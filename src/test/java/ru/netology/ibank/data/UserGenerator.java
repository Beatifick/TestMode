package ru.netology.ibank.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private UserGenerator() {
    }

    // базовый метод
    private static RegistrationDto generateUser(String status) {
        String login = faker.name().username() + faker.number().digits(3);
        String password = faker.internet().password(8, 12);

        RegistrationDto user = new RegistrationDto(login, password, status);

        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return user;
    }

    // активный пользователь
    public static RegistrationDto activeUser() {
        return generateUser("active");
    }

    // заблокированный пользователь
    public static RegistrationDto blockedUser() {
        return generateUser("blocked");
    }

    // пользователь с неправильным паролем
    public static RegistrationDto userWithWrongPassword() {
        RegistrationDto validUser = activeUser();
        return new RegistrationDto(
                validUser.getLogin(),
                faker.internet().password(), // неправильный пароль
                validUser.getStatus()
        );
    }

    // пользователь с неправильным логином
    public static RegistrationDto userWithWrongLogin() {
        RegistrationDto validUser = activeUser();
        return new RegistrationDto(
                faker.name().username(), // неправильный логин
                validUser.getPassword(),
                validUser.getStatus()
        );
    }

    // незарегистрированный пользователь
    public static RegistrationDto unregisteredUser() {
        return new RegistrationDto(
                faker.name().username(),
                faker.internet().password(),
                "active"
        );
    }
}
