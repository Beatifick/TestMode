package ru.netology.ibank.test;

import com.codeborne.selenide.Condition;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.RegistrationDto;
import ru.netology.ibank.data.UserGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeAll
    static void setup() {
        RestAssured.defaultParser = Parser.JSON;
    }

    // Успешный вход
    @Test
    void shouldLoginActiveUser() {
        RegistrationDto user = UserGenerator.activeUser();

        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("h2")
                .shouldBe(visible)
                .shouldHave(text("Личный кабинет"));
    }

    // Заблокированный пользователь
    @Test
    void shouldNotLoginBlockedUser() {
        RegistrationDto user = UserGenerator.blockedUser();

        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    // Неверный пароль
    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto user = UserGenerator.activeUser();

        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue("wrongPass");
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    // Неверный логин
    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto user = UserGenerator.activeUser();

        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue("wrongLogin");
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    // Незарегистрированный пользователь
    @Test
    void shouldNotLoginUnregisteredUser() {
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue("noUser");
        $("[data-test-id=password] input").setValue("noPass");
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
