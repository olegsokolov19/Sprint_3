import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    Order order = CreateRandomOrder.getRandomOrder();
    OrderModel orderModel;

    RequestSpecification requestSpec = new Specification().setRequestSpecification();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
        orderModel = new OrderModel(order.getFirstName(), order.getLastName(), order.getAddress(), order.getMetroStation(),
                order.getPhone(), order.getRentTime(), order.getDeliveryDate(), order.getComment());
    }

    @Test
    @DisplayName("Можно создать заказ на самокат чёрного цвета")
    @Description("Проверяем, что при создании заказа можно заказать самокат чёрного цвета")
    public void createOrderWithBlackColor() {
        orderModel.setColor(new String[]{"BLACK"});

        order.createOrder(orderModel)
                .then().statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Можно создать заказ на самокат серого цвета")
    @Description("Проверяем, что при создании заказа можно заказать самокат серого цвета")
    public void createOrderWithGreyColor() {
        orderModel.setColor(new String[]{"GREY"});

        order.createOrder(orderModel)
                .then().statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Можно создать заказ на самокаты чёрного серого цветов")
    @Description("Проверяем, что при создании заказа можно заказать самокаты чёрного и серого цветов")
    public void createOrderWithBlackAndGreyColor() {
        orderModel.setColor(new String[]{"BLACK", "GREY"});

        order.createOrder(orderModel)
                .then().statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Тело ответа содержит \"track\"")
    @Description("Проверяем, что при создании заказа в теле ответа приходит поле \"track\"")
    public void responseBodyHasTrackField() {
        orderModel.setColor(new String[]{"BLACK", "GREY"});

        order.createOrder(orderModel)
                .then().body("track", notNullValue());
    }

}
