package com.practice.carservice.controller;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.Repair;
import com.practice.carservice.domain.User;
import com.practice.carservice.service.CarService;
import com.practice.carservice.service.EmailService;
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

    private static final String CAR_REG_EMAIL_SCHEMA =
            "Dear %s!\n\n" +
                    "Your car %s %s (%s) has been registered for repair. Thanks for choosing us.\n\n" +
                    "Have a nice day,\n" +
                    "Car Service Team";

    private final RepairService repairService;
    private final CarService carService;
    private final UserServiceImpl userService;
    private final EmailService emailService;

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
        emailService.sendEmail(
                user.getEmail(),
                "Repair registration",
                String.format(
                        CAR_REG_EMAIL_SCHEMA,
                        user.getFirstName(),
                        car.getBrand(),
                        car.getModel(),
                        car.getLicensePlate()));
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
    private static class RepairForm {
        private Car car;
        private String userid;
    }
}
