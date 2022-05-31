import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CourierModel;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest {

    Courier courier = CreateRandomCourier.getRandomCourier();
    CourierModel courierModel;

    int courierId;

    @Before
    public void setUp() {
        courierModel = new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        courier.createCourier(courierModel)
                .then().statusCode(SC_CREATED);

        courierId = courier.logInCourier(courierModel)
                .then().extract()
                .response()
                .path("id");
    }

    @Test
    @DisplayName("Успешный запрос возвращает \"ok: true\"")
    @Description("Проверяем, что успешный запрос возвращает \"ok: true\"")
    public void badRequestReturnError() {
        courier.deleteCourier(courierId)
                .then()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Запрос без ID возвращает ошибку")
    @Description("Проверяем, что запрос без ID вернёт ошибку")
    public void requestWithoutIdReturnError() {
        String expectedError = "Недостаточно данных для удаления курьера";

        given()
                .spec(new Specification().setRequestSpecification())
                .when()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Запрос без ID возвращает ошибку")
    @Description("Проверяем, что запрос без ID вернёт ошибку")
    public void requestWithIncorrectIdReturnError() {
        String expectedError = "Курьера с таким id нет";

        courier.deleteCourier(new Random().nextInt(2_147_483_647))
                .then().statusCode(SC_NOT_FOUND)
                .body("message", equalTo(expectedError));
    }

}
