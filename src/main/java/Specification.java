import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class Specification {
    private final String URL = "https://qa-scooter.praktikum-services.ru";

    public RequestSpecification setRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

}
