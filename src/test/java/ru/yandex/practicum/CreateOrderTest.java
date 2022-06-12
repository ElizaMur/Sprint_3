package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.model.Order;
import ru.yandex.practicum.scooter.api.OrderClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    String track;
    Order order;
    OrderClient orderClient;

    private String black;
    private String grey;

    public CreateOrderTest(String black, String grey) {
            this.black = black;
            this.grey = grey;
    }

    @Parameterized.Parameters
    public static Object[] getTextData() {
            return new Object[][]{
                    {"BLACK", ""},
                    {"GREY", ""},
                    {"BLACK", "GREY"},
                    {"", ""}
            };
        }

        @Test
        @DisplayName("Check create orders")
        public void CreateOrder() {
            order = Order.getRandomOrder();
            List<String> color = Stream.of(black, grey).filter(x -> !x.isBlank()).collect(Collectors.toList());
            order.setColor(color);
            order.setDeliveryDate("2022-06-14T14:40:28.219Z");
            Response responseOrder = OrderClient.createOrder(order);
            assertEquals(SC_CREATED, responseOrder.statusCode());
            track = responseOrder.body().jsonPath().getString("track");
            System.out.println(responseOrder.body().prettyPrint());
        }

}









