package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CarControllerTest {
    @Mock
    private CarService carService;

    @Mock
    private Model model;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateCarPage() {
        String viewName = carController.createCarPage(model);
        
        verify(model).addAttribute(eq("car"), any(Car.class));
        assertEquals("CreateCar", viewName);
    }

    @Test
    public void testCreateCarPost() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);
        
        String viewName = carController.createCarPost(car, model);
        
        verify(carService).create(car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    public void testCreateCarPostWithEmptyFields() {
        Car car = new Car();
        car.setCarId("");
        car.setCarName("");
        car.setCarColor("");
        car.setCarQuantity(0);
        
        String viewName = carController.createCarPost(car, model);
        
        verify(carService).create(car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    public void testCarListPage() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());
        cars.add(new Car());
        
        when(carService.findAll()).thenReturn(cars);
        
        String viewName = carController.carListPage(model);
        
        verify(carService).findAll();
        verify(model).addAttribute(eq("cars"), eq(cars));
        assertEquals("CarList", viewName);
    }

    @Test
    public void testCarListPageWithEmptyList() {
        List<Car> emptyCars = new ArrayList<>();
        when(carService.findAll()).thenReturn(emptyCars);
        
        String viewName = carController.carListPage(model);
        
        verify(carService).findAll();
        verify(model).addAttribute(eq("cars"), eq(emptyCars));
        assertEquals("CarList", viewName);
    }

    @Test
    public void testEditCarPage() {
        String carId = "car-1";
        Car car = new Car();
        car.setCarId(carId);
        
        when(carService.findById(carId)).thenReturn(car);
        
        String viewName = carController.editCarPage(carId, model);
        
        verify(carService).findById(carId);
        verify(model).addAttribute(eq("car"), eq(car));
        assertEquals("EditCar", viewName);
    }

    @Test
    public void testEditCarPageWithComplexCar() {
        String carId = "car-complex";
        Car car = new Car();
        car.setCarId(carId);
        car.setCarName("Luxury Vehicle");
        car.setCarColor("Metallic Blue");
        car.setCarQuantity(3);
        
        when(carService.findById(carId)).thenReturn(car);
        
        String viewName = carController.editCarPage(carId, model);
        
        verify(carService).findById(carId);
        verify(model).addAttribute(eq("car"), eq(car));
        assertEquals("EditCar", viewName);
    }

    @Test
    public void testEditCarPost() {
        Car car = new Car();
        car.setCarId("car-1");
        
        String viewName = carController.editCarPost(car, model);
        
        verify(carService).update(car.getCarId(), car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    public void testEditCarPostWithUpdatedValues() {
        Car car = new Car();
        car.setCarId("car-2");
        car.setCarName("Updated Name");
        car.setCarColor("Updated Color");
        car.setCarQuantity(10);
        
        String viewName = carController.editCarPost(car, model);
        
        verify(carService).update(car.getCarId(), car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    public void testDeleteCar() {
        String carId = "car-1";
        
        String viewName = carController.deleteCar(carId);
        
        verify(carService).delete(carId);
        assertEquals("redirect:listCar", viewName);
    }
    
    @Test
    public void testDeleteCarWithEmptyId() {
        String carId = "";
        
        String viewName = carController.deleteCar(carId);
        
        verify(carService).delete(carId);
        assertEquals("redirect:listCar", viewName);
    }
}
