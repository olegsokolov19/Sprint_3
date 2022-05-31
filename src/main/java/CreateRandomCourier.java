import org.apache.commons.lang3.RandomStringUtils;

public class CreateRandomCourier {

    public static Courier getRandomCourier() {
        String login = RandomStringUtils.randomAlphabetic(12);
        String password = RandomStringUtils.randomAlphabetic(12);
        String firstName = RandomStringUtils.randomAlphabetic(6);

        return new Courier(login, password, firstName);
    }
}
