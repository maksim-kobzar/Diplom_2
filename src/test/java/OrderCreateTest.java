import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

@DisplayName("Создание заказа")
public class OrderCreateTest {

    OrderCreate orderCreate = new OrderCreate();
    UserCreation userCreation = new UserCreation();
    OrderGenerator orderGenerator = new OrderGenerator();
    private String token;
    private User user;
    private Order order;

    @Before
    public void setUp(){
        user = UserGenerator.getDefault();
        order = orderGenerator.getHashIngredient();
    }

    @Test
    @DisplayName("Получение ингридиентов")
    public void getIngredientsTest(){
        ValidatableResponse responseIngredients = orderCreate.getIngredients();
        orderCreate.ingredientHash(responseIngredients.extract().path("data[0]._id"));
        orderCreate.ingredientHash(responseIngredients.extract().path("data[2]._id"));
        responseIngredients.statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void orderNewCreateTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        //Получаем хеш ингредиентов
        ValidatableResponse responseIngredients = orderCreate.getIngredients();
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[0]._id"));
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[2]._id"));

        ValidatableResponse responseOrderNewCreate = orderCreate.orderNewCreate(order, token);
        responseOrderNewCreate.statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизациии")
    public void orderNewCreateNotAuthorizationTest(){
        //Получаем хеш ингредиентов
        ValidatableResponse responseIngredients = orderCreate.getIngredients();
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[0]._id"));
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[2]._id"));

        ValidatableResponse responseOrderNewCreate = orderCreate.orderNewCreate(order, "");
        responseOrderNewCreate.statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизациии")
    public void orderNewCreateNotIngredientsTest(){
        ValidatableResponse responseOrderNewCreate = orderCreate.orderNewCreate(order, "");
        responseOrderNewCreate.statusCode(SC_BAD_REQUEST);
        String errorMassege = responseOrderNewCreate.extract().path("message");
        Assert.assertEquals("Ошибка при создании заказа без ингредиентов", errorMassege, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем")
    public void orderErrorHashTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        //Получаем хеш ингредиентов
        ValidatableResponse responseIngredients = orderCreate.getIngredients();
        orderGenerator.ingredientHash(RandomStringUtils.randomAlphanumeric(24, 24));
        orderGenerator.ingredientHash(RandomStringUtils.randomAlphanumeric(24, 24));
        responseIngredients.statusCode(SC_OK);
        ValidatableResponse responseOrderNewCreate = orderCreate.orderNewCreate(order, token);
        responseOrderNewCreate.statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void userDelete(){
        if (token != null){
            ValidatableResponse responseDelete = userCreation.deleteUser(token);
            responseDelete.assertThat().statusCode(SC_ACCEPTED);
        }
    }

}
