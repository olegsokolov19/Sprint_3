import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {

    Order order = Order.getRandomOrder();
    OrderModel orderModel;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        orderModel = new OrderModel(order.getFirstName(), order.getLastName(), order.getAddress(), order.getMetroStation(),
                order.getPhone(), order.getRentTime(), order.getDeliveryDate(), order.getComment());
    }

    @Test
    @DisplayName("В теле ответа возвращается список заказов")
    @Description("Проверяем, что в теле ответа возвращается список заказов")
    public void requestReturnOrderList() {
        order.createOrder(orderModel)
                .then().statusCode(SC_CREATED);

        order.getOrderList()
                .then().body("orders", notNullValue());
    }

}
