package com.practice.carservice;

import com.practice.carservice.domain.*;
import com.practice.carservice.service.CarPartService;
import com.practice.carservice.service.CarService;
import com.practice.carservice.service.RepairService;
import com.practice.carservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CarServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService,
                          CarPartService carPartService,
                          CarService carService,
                          RepairService repairService) {
        return args -> {
            userService.saveRole(new Role("ROLE_USER"));
            userService.saveRole(new Role("ROLE_MECHANIC"));
            userService.saveRole(new Role("ROLE_ADMIN"));

            User admin = userService.saveUser(new User("admin", "admin", "admin@admin.com", "admin", "admin", "541251", new ArrayList<>()));
            User johnSmith = userService.saveUser(new User("John", "Smith", "johnsmith@example.com", "user", "user", "541252", new ArrayList<>()));
            userService.saveUser(new User("Justin", "Paul", "justinpaul@example.com", "mechanic", "mechanic", "541253", new ArrayList<>()));

            userService.addRoleToUser("admin", "ROLE_USER");
            userService.addRoleToUser("admin", "ROLE_MECHANIC");
            userService.addRoleToUser("admin", "ROLE_ADMIN");
            userService.addRoleToUser("user", "ROLE_USER");
            userService.addRoleToUser("mechanic", "ROLE_USER");
            userService.addRoleToUser("mechanic", "ROLE_MECHANIC");

            CarPart transmission = carPartService.addCarPart(new CarPart("Transmission", "brand3", 3000L));
            CarPart engine = carPartService.addCarPart(new CarPart("Engine", "brand1", 3000L));
            CarPart brakes = carPartService.addCarPart(new CarPart("Brakes", "brand3", 350L));
            CarPart oil = carPartService.addCarPart(new CarPart("Engine Oil", "brand2", 250L));
            CarPart windshield = carPartService.addCarPart(new CarPart("Windshield", "brand3", 1000L));
            CarPart tyre = carPartService.addCarPart(new CarPart("Tyre", "brand4", 500L));

            List<CarPart> carParts = List.of(transmission, engine, brakes, oil, windshield, tyre);

            Car car = carService.addNewCar(new Car(admin, "Lamborghini", "Urus", "URU-599", 2018));
            Car car2 = carService.addNewCar(new Car(johnSmith, "Bmw", "3", "BMW-999", 2010));
            Car car3 = carService.addNewCar(new Car(admin, "Skoda", "Fabia", "SKOD-444", 2006));

            repairService.addNewRepair(new Repair(car, 10, List.of(brakes), "Brakes need a change."));
            repairService.addNewRepair(new Repair(car2, 10, List.of(oil), "Oil change."));
            repairService.addNewRepair(new Repair(car3, 10, carParts, "Big service."));
        };
    }
}
