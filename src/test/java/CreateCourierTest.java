import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CourierModel;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    Courier courier = CreateRandomCourier.getRandomCourier();
    RequestSpecification requestSpec = new Specification().setRequestSpecification();
    
    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }

    @After
    public void deleteCourier() {
        CourierModel courierModel = new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        int statusCode = courier.logInCourier(courierModel)
                .getStatusCode();

        if (statusCode == 200) {
            int courierId = courier.logInCourier(courierModel)
                    .then()
                    .extract()
                    .response()
                    .path("id");

            courier.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера с уникальными данными")
    @Description("Создаём курьера с уникальными данными, которых нет в базе, возвращается 201 статус-код")
    public void createCourierTest() {
        courier.createCourier(new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName()))
                .then()
                .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверяем, что при попытке создать курьера, который будет аналогичен существующему, возвращается 409 статус-код")
    public void createIdenticalCouriersTest() {
        CourierModel courierModel = new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName());

        courier.createCourier(courierModel)
                .then()
                .statusCode(SC_CREATED);

        courier.createCourier(courierModel)
                .then()
                .statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("Создание курьера с незаполненным логином")
    @Description("Проверяем, что курьер не создаётся, если поле \"Логин\" не было заполнено, возвращается 400 статус-код")
    public void createCourierWithInvalidLogin() {
        courier.createCourier(new CourierModel(null, courier.getPassword(), courier.getFirstName()))
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера с незаполненным паролем")
    @Description("Проверяем, что курьер не создаётся, если поле \"Пароль\" не было заполнено, возвращается 400 статус-код")
    public void createCourierWithInvalidPassword() {
        courier.createCourier(new CourierModel(courier.getLogin(), null, courier.getFirstName()))
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера с незаполненными логином и паролем")
    @Description("Проверяем, что курьер не создаётся, если поля \"Логин\" и \"Пароль\" не были заполнены, возвращается 400 статус-код")
    public void createCourierWithInvalidLoginAndPassword() {
        courier.createCourier(new CourierModel(null, null, courier.getFirstName()))
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Успешный запрос на создание курьера возвращает в теле \"ok\": true")
    @Description("Проверяем, что успешное создание курьера возвращает в теле ответа \"ok\": true")
    public void successfulCreateCourierReturnOk() {
        courier.createCourier(new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName()))
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без поля \"Логин\" в теле запроса")
    @Description("Проверяем, что курьер не создаётся, если в теле запроса отсутствует поле \"Логин\", возвращается ошибка")
    public void createCourierWithoutLogin() {
        CourierModel courierModel = new CourierModel();
        courierModel.setPassword(courier.getPassword());
        courierModel.setFirstName(courier.getFirstName());


        String expectedError = "Недостаточно данных для создания учетной записи";

        courier.createCourier(courierModel)
                .then()
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Создание курьера без поля \"Пароль\" в теле запроса")
    @Description("Проверяем, что курьер не создаётся, если в теле запроса отсутствует поле \"Пароль\", возвращается ошибка")
    public void createCourierWithoutPassword() {
        CourierModel courierModel = new CourierModel();
        courierModel.setLogin(courier.getLogin());
        courierModel.setFirstName(courier.getFirstName());

        String expectedError = "Недостаточно данных для создания учетной записи";

        courier.createCourier(courierModel)
                .then()
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Создание курьера без поля \"Имя\" в теле запроса")
    @Description("Проверяем, что курьер не создаётся, если в теле запроса отсутствует поле \"Имя\", возвращается ошибка")
    public void createCourierWithoutLoginAndFirstName() {
        CourierModel courierModel = new CourierModel();
        courierModel.setLogin(courier.getLogin());
        courierModel.setPassword(courier.getPassword());

        String expectedError = "Недостаточно данных для создания учетной записи";

        courier.createCourier(courierModel)
                .then()
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Создание курьера с логином, который уже есть в базе")
    @Description("Проверяем, что курьер не создаётся, если указанный логин уже есть в базе, возвращается ошибка")
    public void createCourierWithNonUniqueLogin() {
        String expectedError = "Этот логин уже используется";

        courier.createCourier(new CourierModel(courier.getLogin(), RandomStringUtils.randomAlphabetic(6)))
                .then()
                .statusCode(SC_CREATED);

        courier.createCourier(new CourierModel(courier.getLogin(), RandomStringUtils.randomAlphabetic(6)))
                .then()
                .body("message", equalTo(expectedError));
    }

}