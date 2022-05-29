import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.OrderModel;


import static io.restassured.RestAssured.given;

public class Order {

    private final String ordersEndpoint = "/api/v1/orders/";

    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    Number rentTime;
    String deliveryDate;
    String comment;
    String[] color;

    public Order(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime, String deliveryDate, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    public Order(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;

    }

    @Step("Создание ордера")
    public Response createOrder(OrderModel orderModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(ordersEndpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .contentType(ContentType.JSON)
                .get(ordersEndpoint);
    }

    @Step("Получение списка заказов по номеру заказа")
    public Response getOrderListByTrackNumber(Number track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("t", track)
                .get(ordersEndpoint + "track/")
                .then()
                .extract()
                .response();
    }

    @Step("Принятие заказа")
    public Response acceptOrder(Number orderId, Number courierId) {
        return given().contentType(ContentType.JSON)
                .queryParam("id", orderId)
                .queryParam("courierId", courierId)
                .when()
                .put(ordersEndpoint + "accept/")
                .then()
                .extract()
                .response();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Number getRentTime() {
        return rentTime;
    }

    public void setRentTime(Number rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

}