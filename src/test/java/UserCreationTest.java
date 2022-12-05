import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.User;
import org.example.UserCreation;
import org.example.UserGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

@DisplayName("Создание пользователя")
public class UserCreationTest {

    private String token;
    private User user;

    UserCreation userCreation = new UserCreation();

    @Before
    public void setUp(){
        user = UserGenerator.getDefault();

    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void userCreationNew(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        responseNewUser.assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание дубликата пользователя")
    public void userDuplicateTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseDuplicateUser = userCreation.createCourier(user);
        responseDuplicateUser.assertThat().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя с незаполненным обязательным полем пароль")
    public void userNotPasswordTest(){
        ValidatableResponse responseNotPassword = userCreation.createCourier(UserGenerator.getNotPasswodr());
        responseNotPassword.assertThat().statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Вход в аккаунт с существующими данными")
    public void userProtectionDataTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseAuthorization = userCreation.authorizationUser(UserGenerator.getAuthorization());

        responseNewUser.assertThat().statusCode(SC_OK);
        responseAuthorization.assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Вход в аккаунт с неверным логином ")
    public void userAuthorizationWrongLoginTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseAuthorization = userCreation.authorizationUser(UserGenerator.getAuthorizationWrongLogin());

        responseNewUser.assertThat().statusCode(SC_OK);
        responseAuthorization.assertThat().statusCode(SC_UNAUTHORIZED);
        String messageResponse = responseAuthorization.extract().path("message");
        Assert.assertEquals("Ошибка в тексте сообщения при неуспешном входе", messageResponse, "email or password are incorrect");
    }

    @Test
    @DisplayName("Вход в аккаунт с неверным паролем ")
    public void userAuthorizationWrongPasswordTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseAuthorization = userCreation.authorizationUser(UserGenerator.getAuthorizationWrongPassword());

        responseNewUser.assertThat().statusCode(SC_OK);
        responseAuthorization.assertThat().statusCode(SC_UNAUTHORIZED);
        String messageResponse = responseAuthorization.extract().path("message");
        Assert.assertEquals("Ошибка в тексте сообщения при неуспешном входе", messageResponse, "email or password are incorrect");
    }

    @Test
    @DisplayName("Изменение данных пользоватяле - изменение email")
    public void userChangesEmailTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        String email1 = responseNewUser.extract().path("user.email");
        ValidatableResponse responseChanges = userCreation.changesUser(UserGenerator.getChangesEmail(), token);
        responseNewUser.assertThat().statusCode(SC_OK);
        responseChanges.assertThat().statusCode(SC_OK);
        String email2 = responseChanges.extract().path("user.email");

        Assert.assertNotEquals("Ошибка при изменении email", email1, email2);
    }

    @Test
    @DisplayName("Изменение данных пользоватяле - изменение name")
    public void userChangesNameTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        String name1 = responseNewUser.extract().path("user.name");
        ValidatableResponse responseChanges = userCreation.changesUser(UserGenerator.getChangesName(), token);
        responseNewUser.assertThat().statusCode(SC_OK);
        responseChanges.assertThat().statusCode(SC_OK);
        String name2 = responseChanges.extract().path("user.name");

        Assert.assertNotEquals("Ошибка при изменении name", name1, name2);
    }

    @Test
    @DisplayName("Изменение данных пользоватяле - изменение name без авторизации")
    public void userChangesNameNotTokenTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseChanges = userCreation.changesUser(UserGenerator.getChangesName(), "");
        responseNewUser.assertThat().statusCode(SC_OK);
        responseChanges.assertThat().statusCode(SC_UNAUTHORIZED);
        String messageResponse = responseChanges.extract().path("message");

        Assert.assertEquals("Ошибка при изменении name без передачи токена", messageResponse, "You should be authorised");
    }

    @Test
    @DisplayName("Изменение данных пользоватяле - изменение email без авторизации")
    public void userChangesEmailNotTokenTest(){
        ValidatableResponse responseNewUser = userCreation.createCourier(user);
        token = responseNewUser.extract().path("accessToken");
        ValidatableResponse responseChanges = userCreation.changesUser(UserGenerator.getChangesEmail(), "");
        responseNewUser.assertThat().statusCode(SC_OK);
        responseChanges.assertThat().statusCode(SC_UNAUTHORIZED);
        String messageResponse = responseChanges.extract().path("message");

        Assert.assertEquals("Ошибка при изменении email без передачи токена", messageResponse, "You should be authorised");
    }

    @After
    public void userDelete(){
        if (token != null){
            ValidatableResponse responseDelete = userCreation.deleteUser(token);
            responseDelete.assertThat().statusCode(SC_ACCEPTED);
        }
    }
}
