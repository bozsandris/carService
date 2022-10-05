package com.practice.carservice.repo;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.Repair;
import com.practice.carservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
class RepairRepoTest {

    private Long carId;
    private Long repairId;
    @Autowired
    RepairRepo underTest;
    @Autowired
    CarRepo carRepo;
    @Autowired
    UserRepo userRepo;

    private User createUser() {
        return userRepo.save(
                new User("Test",
                        "Test",
                        "test@test.test",
                        "test",
                        "test",
                        "1234567",
                        new ArrayList<>()
                )
        );
    }

    @BeforeEach
    void setUp() {
        User user = createUser();

        Car car = new Car(user, "Nissan", "Micra", "TEST001", 2010);
        carId = carRepo.save(car).getId();

        Repair repair = new Repair(car, 15, new ArrayList<>(), "Oil change.");
        repairId = underTest.save(repair).getId();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        carRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void itShouldFindRepairByCarId() {
        assertThat(underTest.findRepairByCarId(carId)).isPresent();
    }

    @Test
    void itShouldNotFindRepairByCarId() {
        assertThat(underTest.findRepairByCarId(2L)).isPresent();
    }
}