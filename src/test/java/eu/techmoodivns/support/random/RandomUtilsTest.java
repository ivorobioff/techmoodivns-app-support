package eu.techmoodivns.support.random;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static eu.techmoodivns.support.random.RandomUtils.transportProperties;

public class RandomUtilsTest {

    @Test
    public void testTransportProperties() {
        var car1 = new Car("Focus", "Ford", 4000.99d);
        var car2 = new Car("R8", "Audi", 8000.99d);

        transportProperties(car1, car2, Set.of("price"));

        assertEquals("Focus", car2.name);
        assertEquals("Audi", car2.brand);
        assertEquals(8000.99d, car2.price);
    }

    public static class Car {
        private static double rate = 0.1d;
        private final String brand;
        private String name;
        private double price;

        public Car(String name, String brand, double price) {
            this.name = name;
            this.brand = brand;
            this.price = price;
        }
    }
}
