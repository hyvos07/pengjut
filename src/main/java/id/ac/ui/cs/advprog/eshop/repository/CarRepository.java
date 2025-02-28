package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class CarRepository {
    static int id = 0;
    private List<Car> carData = new ArrayList<>();

    public Car create(Car car){
        carData.add(car);
        
        return car;
    }

    public Iterator<Car> findAll(){
        return carData.iterator();
    }

    public Car findById(String carId){
        for(Car car : carData){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }

        return null;
    }

    public Car update(String id, Car updatedCar){
        for (int i = 0; i < carData.size(); i++){
            Car car = carData.get(i);
            if (car.getCarId().equals(id)){
                // Change if same
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());
                
                return updatedCar;
            }
        }

        return null; // When there is no car with the given id
    }

    public void delete(String id){
        carData.removeIf(car -> car.getCarId().equals(id));
    }
}
