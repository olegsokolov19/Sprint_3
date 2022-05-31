import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CourierModel;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListByTrackNumberTest {

    Order order = CreateRandomOrder.getRandomOrder();
    OrderModel orderModel;

    Courier courier = CreateRandomCourier.getRandomCourier();
    CourierModel courierModel;

    int courierId;
    int orderId;

    @Before
    public void setUp() {
        courierModel = new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        orderModel = new OrderModel(courier.getFirstName(), order.getLastName(), order.getAddress(), order.getMetroStation(),
                order.getPhone(), order.getRentTime(), order.getDeliveryDate(), order.getComment());

        courier.createCourier(courierModel).then().statusCode(SC_CREATED);
        courierId = courier.logInCourier(courierModel)
                .then()
                .extract()
                .response()
                .path("id");

        orderId = order.createOrder(orderModel)
                .then()
                .extract()
                .path("track");
    }

    @Test
    @DisplayName("Успешный запрос возвращет объект с заказом")
    @Description("Проверяем, что успешный запрос возвращет объект с заказом")
    public void successRequestReturnOrderObject() {
        order.getOrderListByTrackNumber(orderId)
                .then()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку")
    @Description("Проверяем, что запрос без номера заказа возвращает ошибку")
    public void requestWithoutOrderIdReturnError() {
        String expectedError = "Недостаточно данных для поиска";

        order.getOrderListByTrackNumber(null)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Запрос с номером несуществующего заказа возвращает ошибку")
    @Description("Проверяем, что запрос с номером несуществующего заказа возвращает ошибку")
    public void requestWithIncorrectOrderIdReturnError() {
        String expectedError = "Заказ не найден";

        order.getOrderListByTrackNumber(new Random().nextInt(10_000_000))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo(expectedError));
    }

}
