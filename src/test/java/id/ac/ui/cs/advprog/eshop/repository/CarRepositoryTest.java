package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {
    @InjectMocks
    private CarRepository carRepository;
    
    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }
    
    @Test
    void testCreateCarWithoutId() {
        Car car = new Car();
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(10);
        
        Car result = carRepository.create(car);
        
        assertNotNull(result.getCarId());
        assertEquals("Toyota", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }
    
    @Test
    void testCreateCarWithId() {
        String existingId = "existing-id";
        Car car = new Car();
        car.setCarId(existingId);
        car.setCarName("Honda");
        
        Car result = carRepository.create(car);
        
        assertEquals(existingId, result.getCarId());
    }
    
    @Test
    void testFindAllEmpty() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    void testFindAllWithCars() {
        createSampleCar("Car1");
        createSampleCar("Car2");
        
        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        
        assertEquals(2, count);
    }
    
    @Test
    void testFindByIdExisting() {
        Car car = createSampleCar("Mazda");
        String carId = car.getCarId();
        
        Car foundCar = carRepository.findById(carId);
        
        assertNotNull(foundCar);
        assertEquals("Mazda", foundCar.getCarName());
    }
    
    @Test
    void testFindByIdNonExisting() {
        Car foundCar = carRepository.findById("non-existing-id");
        assertNull(foundCar);
    }
    
    @Test
    void testUpdateExistingCar() {
        Car car = createSampleCar("Original");
        car.setCarColor("Black");
        car.setCarQuantity(1);
        
        Car updatedCar = new Car();
        updatedCar.setCarName("Updated");
        updatedCar.setCarColor("White");
        updatedCar.setCarQuantity(2);
        
        Car result = carRepository.update(car.getCarId(), updatedCar);
        
        assertNotNull(result);
        assertEquals("Updated", result.getCarName());
        
        Car foundCar = carRepository.findById(car.getCarId());
        assertEquals("Updated", foundCar.getCarName());
        assertEquals("White", foundCar.getCarColor());
        assertEquals(2, foundCar.getCarQuantity());
    }
    
    @Test
    void testUpdateNonExistingCar() {
        Car updatedCar = new Car();
        updatedCar.setCarName("Updated");
        
        Car result = carRepository.update("non-existing-id", updatedCar);
        
        assertNull(result);
    }
    
    @Test
    void testDeleteExistingCar() {
        Car car = createSampleCar("ToDelete");
        String carId = car.getCarId();
        
        assertNotNull(carRepository.findById(carId));
        
        carRepository.delete(carId);
        
        assertNull(carRepository.findById(carId));
    }
    
    @Test
    void testDeleteNonExistingCar() {
        assertDoesNotThrow(() -> carRepository.delete("non-existing-id"));
    }
    
    private Car createSampleCar(String name) {
        Car car = new Car();
        car.setCarName(name);
        return carRepository.create(car);
    }
}
