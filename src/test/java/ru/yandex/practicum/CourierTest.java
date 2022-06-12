package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.scooter.api.CourierClient;
import ru.yandex.practicum.scooter.api.model.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.scooter.api.model.Courier.getRandomCourier;
import static ru.yandex.practicum.scooter.api.model.CourierWithRequiredFields.getRandomCourierRequiredFields;

public class CourierTest {
    int courierId;
    Courier courier;
    CourierWithRequiredFields courierWithRequiredFields;
    CourierClient courierClient;
    String errorMessage;

    @Before
    public void init() {
        courier = getRandomCourier();
        courierClient = new CourierClient();
        courierWithRequiredFields = getRandomCourierRequiredFields();
    }

    @After
    public void clear() {
        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }
    //Создание курьера
    @Test
    @DisplayName("Check create courier")
    public void createCourierTest() {
        //Делаем действие
        Response responseCreate = courierClient.createCourier(courier);
        assertEquals(SC_CREATED, responseCreate.statusCode());
        CreateCourierResponse createCourierResponse = responseCreate.as(CreateCourierResponse.class);
        assertEquals(true, createCourierResponse.ok);
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_OK, responseLogin.statusCode());
        courierId = responseLogin.body().jsonPath().getInt("id");
    }

    //Создание курьера с одинаковым логином
    @Test
    @DisplayName("Check create courier with used data")
    public void createTheSameCourierTest() {
        //Делаем действие
        courierClient.createCourier(courier);
        courier = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        Response responseCreate = courierClient.createCourier(courier);
        assertEquals(SC_CONFLICT, responseCreate.statusCode());
        errorMessage = responseCreate.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    //Создание курьера с обязательными полями
    @Test
    @DisplayName("Check create courier with required data")
    public void createCourierTestWithRequiredFields() {

        Response responseCreate = courierClient.createCourierWithRequiredFields(courierWithRequiredFields);
        assertEquals(SC_CREATED, responseCreate.statusCode());
        CreateCourierResponse createCourierResponse = responseCreate.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.ok);
        CourierCredentials courierCredentials = new CourierCredentials(courierWithRequiredFields.getLogin(), courierWithRequiredFields.getPassword());
        Response responseLogin = courierClient.login(courierCredentials);
        assertEquals(SC_OK, responseLogin.statusCode());
        courierId = responseLogin.body().jsonPath().getInt("id");
    }
    //Создание курьера с пустым полем
    @Test
    @DisplayName("Check create courier without required fields")
    public void createCourierTestWithoutRequiredFields() {
        Courier courier = new Courier("", "password", "Murzina");
        Response responseCreate = courierClient.createCourier(courier);
        assertEquals(SC_BAD_REQUEST, responseCreate.statusCode());
        errorMessage = responseCreate.body().jsonPath().getString("message");
        assertThat(errorMessage, equalTo("Недостаточно данных для создания учетной записи"));
    }

}