import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import static org.apache.http.HttpStatus.SC_CREATED;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {null}
        };
    }

    Order order = CreateRandomOrder.getRandomOrder();
    OrderModel orderModel;

    @Before
    public void setUp() {
        orderModel = new OrderModel(order.getFirstName(), order.getLastName(), order.getAddress(), order.getMetroStation(),
                order.getPhone(), order.getRentTime(), order.getDeliveryDate(), order.getComment());
    }

    @Test
    @DisplayName("Можно создать заказ на самокат с выбранным цветом или без выбора цвета")
    @Description("Проверяем, что при создании заказа можно заказать чёрный или серый самокат, чёрный и серый сразу или вообще не указывать цвет")
    public void createOrderWithBlackColor() {
        orderModel.setColor(color);

        order.createOrder(orderModel)
                .then().statusCode(SC_CREATED);
    }

}
