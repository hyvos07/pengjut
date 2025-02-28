package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarTest {
    Car car;
    
    @BeforeEach
    void setUp() {
        car = new Car();
    }
    
    @Test
    void testGetAndSetCarId() {
        String carId = "car-123";
        car.setCarId(carId);
        assertEquals(carId, car.getCarId());
    }
    
    @Test
    void testGetAndSetCarName() {
        String carName = "Toyota Avanza";
        car.setCarName(carName);
        assertEquals(carName, car.getCarName());
    }
    
    @Test
    void testGetAndSetCarColor() {
        String carColor = "Silver";
        car.setCarColor(carColor);
        assertEquals(carColor, car.getCarColor());
    }
    
    @Test
    void testGetAndSetCarQuantity() {
        int carQuantity = 5;
        car.setCarQuantity(carQuantity);
        assertEquals(carQuantity, car.getCarQuantity());
    }
}
