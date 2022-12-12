package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserCreation extends Client {

    @Step("Создание нового пользователя")
    public ValidatableResponse createCourier(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post("api/auth/register")
                .then();
    }
    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token){
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .when()
                .delete("api/auth/user" )
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse authorizationUser(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post("api/auth/login" )
                .then();
    }

    @Step("Обнавления данных о пользователе")
    public ValidatableResponse changesUser(User user, String token){
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .body(user)
                .when()
                .patch("api/auth/user")
                .then();
    }
    @Step("Запрос заказов пользователя")
    public ValidatableResponse getUserOrders(String token){
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .when()
                .get("api/orders")
                .then();
    }

}
