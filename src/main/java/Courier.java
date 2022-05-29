import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.CourierModel;

import static io.restassured.RestAssured.given;

public class Courier {
    private final String courierEndpoint = "/api/v1/courier/";

    private String login;
    private String password;
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Response createCourier(CourierModel courierModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .when()
                .post(courierEndpoint)
                .then()
                .extract()
                .response();
    }

    public Response logInCourier(CourierModel courierModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierModel)
                .when()
                .post(courierEndpoint + "login")
                .then()
                .extract()
                .response();
    }

    public Response deleteCourier(int courierId) {
        return given()
                .contentType(ContentType.JSON)
                .delete(courierEndpoint + courierId)
                .then()
                .extract()
                .response();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
