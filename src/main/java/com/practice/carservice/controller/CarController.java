package com.practice.carservice.controller;

import com.practice.carservice.domain.Car;
import com.practice.carservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping(path = {"user/{userId}"})
    public ResponseEntity<List<Car>> getUsersCars(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(carService.getCarsByUserId(userId));
    }

    @GetMapping(path = {"{carId}"})
    public Car getCarById(@PathVariable("carId") Long carId) {
        return carService.getCarById(carId);
    }

    @PutMapping(path = "{carId}")
    public void updateCar(@RequestBody Car car) {
        carService.updateCar(car);
    }
}
