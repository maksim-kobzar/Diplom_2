package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderCreate extends Client {
    @Step("Создание заказа")
    public ValidatableResponse orderNewCreate(Order order, String token){
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .body(order)
                .when()
                .post("api/orders")
                .then();
    }

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredients(){
        return given()
                .spec(getSpec())
                .when()
                .get("api/ingredients")
                .then();
    }

    public List<String> ingredient = new ArrayList<String>();
    public void ingredientHash(String ingredientHash){
        ingredient.add(ingredientHash);

    }

}
