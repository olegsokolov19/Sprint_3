import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practicum.scooter.api.model.OrderModel;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class Order {

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

    public static Order getRandomOrder() {
        Random random = new Random();

        String firstName = RandomStringUtils.randomAlphabetic(8);
        String lastName = RandomStringUtils.randomAlphabetic(8);
        String address = RandomStringUtils.randomAlphabetic(8);
        String metroStation = RandomStringUtils.randomAlphabetic(6);
        String phone = RandomStringUtils.randomAlphabetic(9);
        Number rentTime = random.nextInt(7);
        String deliveryDate = String.format("%s-%s-%s", random.nextInt(2022), random.nextInt(11) + 1, random.nextInt(30) + 1);
        String comment = RandomStringUtils.randomAlphabetic(12);

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
    }


    public Response createOrder(OrderModel orderModel) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract()
                .response();
    }

    public Response getOrderList() {
        return given()
                .contentType(ContentType.JSON)
                .get("/api/v1/orders");
    }

    public Response getOrderListByTrackNumber(Number track) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("t", track)
                .get("/api/v1/orders/track")
                .then()
                .extract()
                .response();
    }

    public Response acceptOrder(Number orderId, Number courierId) {
        return given().contentType(ContentType.JSON)
                .queryParam("id", orderId)
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/")
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