package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.scooter.api.OrderClient;
import ru.yandex.practicum.scooter.api.model.Order;
import ru.yandex.practicum.scooter.api.model.OrdersList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;


public class GetOrdersTest {
    int track;
    Order order;
    OrderClient orderClient;

        @Test
        @DisplayName("Check get list of orders")
        public void getOrdersList() {
            order = Order.getRandomOrder();
            OrderClient.createOrder(order);
            Response responseOrder = OrderClient.getOrders();
            assertEquals(SC_OK, responseOrder.statusCode());
            responseOrder.body().jsonPath().getList("orders", OrdersList.class);
            assertThat(responseOrder, notNullValue());
        }

}









