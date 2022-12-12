import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

@DisplayName("Получение заказов конкретного пользователя")
public class GetUserOrders {
    private String token;
    private User user;
    UserCreation userCreation = new UserCreation();
    OrderGenerator orderGenerator = new OrderGenerator();
    OrderCreate orderCreate = new OrderCreate();
    private Order order;

    @Before
    public void setUp(){
        user = UserGenerator.getDefault();
        order = orderGenerator.getHashIngredient();
    }

    @Test
    @DisplayName("Получить заказ авторизованного пользователя")
    public void getOrderAuthorisationUser(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");

        ValidatableResponse responseIngredients = orderCreate.getIngredients();
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[0]._id"));
        orderGenerator.ingredientHash(responseIngredients.extract().path("data[2]._id"));
        ValidatableResponse responseOrderNewCreate = orderCreate.orderNewCreate(order, token);
        ValidatableResponse responseOrder = userCreation.getUserOrders(token);

        responseOrder.statusCode(SC_OK);
        Assert.assertNotNull("Ошибка при получении заказа авторизованного пользователя ", responseOrder.extract().path("orders"));
    }

    @Test
    @DisplayName("Получить заказ не авторизованного пользователя")
    public void getOrderNotAuthorisationUser(){
        ValidatableResponse responseOrder = userCreation.getUserOrders("");
        boolean responseSuccess = responseOrder.extract().path("success");
        Assert.assertFalse("Ошибка при получении заказа не авторизованного пользователя", responseSuccess);
        responseOrder.statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void userDelete(){
        if (token != null){
            ValidatableResponse responseDelete = userCreation.deleteUser(token);
            responseDelete.assertThat().statusCode(SC_ACCEPTED);
        }
    }
}
