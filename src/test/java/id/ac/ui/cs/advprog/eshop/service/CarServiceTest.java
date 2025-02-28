package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    
    @Mock
    private CarRepository carRepository;
    
    @InjectMocks
    private CarService carService;
    
    private Car car1;
    private Car car2;
    
    @BeforeEach
    void setUp() {
        car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("Toyota Avanza");
        car1.setCarColor("Black");
        car1.setCarQuantity(10);
        
        car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Red");
        car2.setCarQuantity(5);
    }
    
    @Test
    void testCreateCar() {
        when(carRepository.create(car1)).thenReturn(car1);
        
        Car createdCar = carService.create(car1);
        
        verify(carRepository, times(1)).create(car1);
        assertEquals(car1, createdCar);
    }
    
    @Test
    void testFindAllCars() {
        Iterator<Car> carIterator = Arrays.asList(car1, car2).iterator();
        when(carRepository.findAll()).thenReturn(carIterator);
        
        List<Car> allCars = carService.findAll();
        
        verify(carRepository, times(1)).findAll();
        assertEquals(2, allCars.size());
        assertTrue(allCars.contains(car1));
        assertTrue(allCars.contains(car2));
    }
    
    @Test
    void testFindCarById() {
        when(carRepository.findById("car-1")).thenReturn(car1);
        
        Car foundCar = carService.findById("car-1");
        
        verify(carRepository, times(1)).findById("car-1");
        assertEquals(car1, foundCar);
    }
    
    @Test
    void testUpdateCar() {
        carService.update("car-1", car2);
        
        verify(carRepository, times(1)).update("car-1", car2);
    }
    
    @Test
    void testDeleteCar() {
        carService.delete("car-1");
        
        verify(carRepository, times(1)).delete("car-1");
    }
}
