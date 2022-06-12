package ru.yandex.practicum.scooter.api;

import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.BaseApiClient;
import ru.yandex.practicum.scooter.api.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {

    public static Response createOrder(Order order) {
        return given()
                .spec(getReqSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/v1/orders");
    }

    public static Response getOrders() {
        return given()
                .spec(getReqSpec())
                .when()
                .get(BASE_URL + "/api/v1/orders");
    }


}


