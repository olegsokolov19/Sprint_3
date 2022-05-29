import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;


public class Specification {
    private final String URL = "https://qa-scooter.praktikum-services.ru";

    public RequestSpecification setRequestSpecification() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(URL)
                .build();
        return requestSpec;
    }

}
