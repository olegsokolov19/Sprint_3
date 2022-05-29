import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CourierModel;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest {

    Order order = CreateRandomOrder.getRandomOrder();
    OrderModel orderModel;

    Courier courier = CreateRandomCourier.getRandomCourier();
    CourierModel courierModel;

    int courierId;
    int orderId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
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
    @DisplayName("Успешный запрос возвращает \"ok: true\"")
    @Description("Проверяем, что успешный запрос возвращает \"ok: true\"")
    public void successRequestReturnTrue() {
        order.acceptOrder(orderId, courierId)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Запрос без ID курьера возвращает ошибку")
    @Description("Проверяем, что запрос без ID курьера возвращает ошибку")
    public void acceptOrderWithoutCourierIdReturnError() {
        String expectedError = "Недостаточно данных для поиска";

        order.acceptOrder(orderId, null)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Запрос с неверным ID курьера вернёт ошибку")
    @Description("Проверяем, что запрос с неверным ID курьера вернёт ошибку")
    public void acceptOrderWithIncorrectCourierId() {
        String expectedError = "Курьера с таким id не существует";

        order.acceptOrder(orderId, new Random().nextInt(1_000_000))
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Запрос без ID заказа возвращает ошибку")
    @Description("Проверяем, что запрос без ID заказа возвращает ошибку")
    public void acceptOrderWithoutOrderIdReturnError() {
        String expectedError = "Недостаточно данных для поиска";

        order.acceptOrder(null, courierId)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Запрос с неверным ID заказа вернёт ошибку")
    @Description("Проверяем, что запрос с неверным ID заказа вернёт ошибку")
    public void acceptOrderWithIncorrectOrderId() {
        String expectedError = "Заказа с таким id не существует";

        order.acceptOrder(new Random().nextInt(10_000_000), courierId)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo(expectedError));
    }

}
