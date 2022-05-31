import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class CreateRandomOrder {

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
}
