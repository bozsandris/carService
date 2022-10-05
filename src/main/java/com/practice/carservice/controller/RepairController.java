package com.practice.carservice.controller;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.Repair;
import com.practice.carservice.domain.User;
import com.practice.carservice.service.CarService;
import com.practice.carservice.service.RepairService;
import com.practice.carservice.service.UserServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/repair")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;
    private final CarService carService;
    private final UserServiceImpl userService;

    @GetMapping(path = {"car/{carId}"})
    public Repair getRepairForCarId(@PathVariable("carId") Long carId) {
        return repairService.getRepairByCarId(carId);
    }

    @PostMapping
    public ResponseEntity<?> registerRepairForCar(@RequestBody RepairForm form) {
        User user = userService.getUserById(Long.valueOf(form.getUserid()));
        Car car = form.getCar();
        car.setUser(user);
        Repair repair = car.getRepair();
        repair.setCar(carService.addNewCar(car));
        repairService.addNewRepair(repair);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/repair").toUriString());
        return ResponseEntity.created(uri).body(repair);
    }

    @GetMapping(path = "/manage")
    public ResponseEntity<?> getRepairs() {
        return ResponseEntity.ok().body(repairService.getRepairs());
    }


    @PutMapping(path = "/manage/edit")
    public ResponseEntity<?> updateRepairForCar(@RequestBody Repair repair) {
        repairService.updateRepair(repair);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/manage/delete")
    public ResponseEntity<?> deleteRepairForCar(@RequestParam Long carId) {
        carService.deleteCarAndRepairById(carId);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class RepairForm {
        private Car car;
        private String userid;
    }
}
