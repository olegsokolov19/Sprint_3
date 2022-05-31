import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.CourierModel;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginCourierTest {

    Courier courier = CreateRandomCourier.getRandomCourier();

    int statusCode;
    int courierId;

    @Before
    public void setUp() {
        Response response = courier.createCourier(new CourierModel(courier.getLogin(), courier.getPassword(), courier.getFirstName()))
                .then().extract().response();

        statusCode = response.getStatusCode();
        if (statusCode == SC_OK) {
            courierId = response.path("id");
        }
    }

    @After
    public void deleteCourier() {
        if(statusCode == SC_OK) {
            given()
                    .spec(new Specification().setRequestSpecification())
                    .delete("/api/v1/courier/" + courierId)
                    .then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверяем, что курьер может авторизоваться")
    public void logInCourier() {
        courier.logInCourier(new CourierModel(courier.getLogin(), courier.getPassword()))
                .then().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация курьера с логином = <null>")
    @Description("Проверяем, что курьер не может авторизоваться, если логин = null")
    public void logInCourierWithNullLogin() {
        courier.logInCourier(new CourierModel(null, courier.getPassword()))
                .then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация курьера с паролем = <null>")
    @Description("Проверяем, что курьер не может авторизоваться, если пароль = null")
    public void logInCourierWithNullPassword() {
        courier.logInCourier(new CourierModel(courier.getLogin(), courier.getPassword()))
                .then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация курьера с логином и паролем = <null>")
    @Description("Проверяем, что курьер не может авторизоваться, если логин и пароль = null")
    public void logInCourierWithNullLoginAndPassword() {
        courier.logInCourier(new CourierModel(null, null))
                .then().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим логином")
    @Description("Проверяем, что курьер не может авторизоваться, если логин указан неверно, возвращается 404 статус-код")
    public void logInCourierWithIncorrectLogin() {
        courier.logInCourier(new CourierModel("IncorrectLogin", courier.getPassword()))
                .then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным паролем")
    @Description("Проверяем, что курьер не может авторизоваться, если пароль указан неверно, возвращается 404 статус-код")
    public void logInCourierWithIncorrectPassword() {
        courier.logInCourier(new CourierModel(courier.getLogin(), "IncorrectPassword"))
                .then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация курьера без поля \"Логин\"")
    @Description("Проверяем, что на запрос без поля \"Логин\" возвращается 400 статус-код и ошибка")
    public void logInCourierWithoutLogin() {
        String expectedError = "Недостаточно данных для входа";

        CourierModel courierModel = new CourierModel();
        courierModel.setPassword(courier.getPassword());

        courier.logInCourier(courierModel)
                .then().body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Авторизация курьера без поля \"Пароль\"")
    @Description("Проверяем, что на запрос без поля \"Пароль\" возвращается 400 статус-код и ошибка \"Недостаточно данных для входа\"")
    public void logInCourierWithoutPassword() {
        String expectedError = "Недостаточно данных для входа";

        CourierModel courierModel = new CourierModel();
        courierModel.setLogin(courier.getLogin());

        courier.logInCourier(courierModel)
                .then().statusCode(SC_BAD_REQUEST)
                .body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Ошибка при вводе несуществующих данных при авторизации")
    @Description("Проверяем, что при вводе несуществующих данных в ответе возвращается ошибка")
    public void logInCourierWithIncorrectLoginAndPassword() {
        String expectedError = "Учетная запись не найдена";

        String login = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);

        courier.logInCourier(new CourierModel(login, password))
                .then().body("message", equalTo(expectedError));
    }

    @Test
    @DisplayName("Успешная авторизация возвращает ID в ответе")
    @Description("Проверяем, что при успешной авторизации в ответе возвращается ID курьера")
    public void successLogInCourierReturnId() {
        courier.logInCourier(new CourierModel(courier.getLogin(), courier.getPassword()))
                .then().body("id", notNullValue());
    }

}
