package com.practice.carservice.service;

import com.practice.carservice.domain.Car;
import com.practice.carservice.repo.CarRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class CarService {

    private final CarRepo carRepository;

    @Autowired
    public CarService(CarRepo carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getCarsByUserId(Long userId) {
        log.info("Fetching cars");
        return carRepository.findCarsByUserId(userId);
    }

    public Car getCarById(Long carId) {
        log.info("Fetching cars");
        return carRepository.findById(carId).orElseThrow(() -> new IllegalStateException("Car not found"));
    }

    public Car addNewCar(Car car) {
        if (carRepository.findCarByLicensePlate(car.getLicensePlate()).isPresent()) {
            throw new IllegalStateException("Car with that license plate is already registered");
        }
        log.info("Saving car with license {}", car.getLicensePlate());
        return carRepository.save(car);
    }

    public void updateCar(Car car) {
        carRepository.findById(car.getId())
                .orElseThrow(() -> new IllegalStateException("Car with id: " + car.getId() + " does not exists"));

        if (car.getBrand().isEmpty()) {
            throw new IllegalStateException("brand can't be empty");
        }

        if (car.getModel().isEmpty()) {
            throw new IllegalStateException("model can't be empty");
        }

        if (car.getProdYear() <= 1940 || car.getProdYear() >= LocalDate.now().getYear()) {
            throw new IllegalStateException("invalid year of production");
        }

        if (car.getLicensePlate().isEmpty()) {
            throw new IllegalStateException("license plate can't be empty");
        }

        if (carRepository.findCarByLicensePlate(car.getLicensePlate()).isPresent()) {
                throw new IllegalStateException("license plate is taken");
        }

        log.info("Updating car details with id {}", car.getId());

        carRepository.save(car);
    }

    public void deleteCarAndRepairById(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalStateException("Car with id " + carId + " not found"));

        log.info("Deleting car and repair with car id {}", carId);
        carRepository.delete(car);
    }
}
