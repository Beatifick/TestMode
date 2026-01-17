package ru.netology.ibank.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.RegistrationDto;
import ru.netology.ibank.data.UserGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginActiveUser() {
        RegistrationDto user = UserGenerator.activeUser();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("h2")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    void shouldNotLoginBlockedUser() {
        RegistrationDto user = UserGenerator.blockedUser();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto user = UserGenerator.userWithWrongPassword();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto user = UserGenerator.userWithWrongLogin();

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginUnregisteredUser() {
        RegistrationDto user = UserGenerator.unregisteredUser();
        
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
