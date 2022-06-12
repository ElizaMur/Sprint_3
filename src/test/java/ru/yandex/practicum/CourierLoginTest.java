package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.scooter.api.CourierClient;
import ru.yandex.practicum.scooter.api.model.Courier;
import ru.yandex.practicum.scooter.api.model.CourierCredentials;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static ru.yandex.practicum.scooter.api.model.Courier.getRandomCourier;
import static ru.yandex.practicum.scooter.api.model.CourierWithRequiredFields.getRandomCourierRequiredFields;

public class CourierLoginTest {
    int courierId;
    Courier courier;
    CourierClient courierClient;
    String errorMessage;
    CourierCredentials courierCredentials;

    @Before
    public void init() {
        courier = getRandomCourier();
        courierClient = new CourierClient();
        courierCredentials = CourierCredentials.getRandomCourierCredentials();
    }

    @After
    public void clear() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }



    @Test
    @DisplayName("Check authorization courier")
    public void courierLoginTest() {
        //Делаем действие
        courierClient.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_OK, responseLogin.statusCode());
        assertThat(responseLogin.body().jsonPath().getInt("id"), notNullValue());
        courierId = responseLogin.body().jsonPath().getInt("id");
    }
    //Авторизация несуществующего пользователя
    @Test
    @DisplayName("Check authorization with incorrect login")
    public void loginCourierTestWithIncorrectLogin() {
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        errorMessage = responseLogin.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Учетная запись не найдена"));
    }

    //Авторизация с неккоректным паролем
    @Test
    @DisplayName("Check authorization with incorrect password")
    public void loginCourierTestWithIncorrectPassword() {
        courierClient.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), getRandomCourierRequiredFields().getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLogin.statusCode());
        errorMessage = responseLogin.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Учетная запись не найдена"));
    }

    //Авторизация с пустым паролем
    @Test
    @DisplayName("Check authorization with empty fields")
    public void loginCourierTestWithEmptyFields() {
        courierClient.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), "");
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        errorMessage = responseLogin.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Недостаточно данных для входа"));
    }

    //Авторизация с пустым логином
    @Test
    @DisplayName("Check authorization with empty login fields")
    public void loginCourierTestWithEmptyLogin() {
        courierClient.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials("",courier.getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_BAD_REQUEST, responseLogin.statusCode());
        errorMessage = responseLogin.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Недостаточно данных для входа"));
    }
}