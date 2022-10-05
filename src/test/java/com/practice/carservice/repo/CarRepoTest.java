package com.practice.carservice.repo;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
class CarRepoTest {

    private final String licensePlate = "ABH123";
    private Long userId;
    private Long carId;
    @Autowired
    private CarRepo underTest;
    @Autowired
    private UserRepo userRepo;

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
        userId = user.getId();
        Car car = new Car(user, "Nissan", "Micra", licensePlate, 2010);
        carId = underTest.save(car).getId();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void itShouldFindCarsByUserId() {
        List<Car> resultList = underTest.findCarsByUserId(userId);
        assertThat(resultList).asList().hasSize(1);
    }

    @Test
    void itShouldNotFindCarsByUserId() {
        assertThat(underTest.findCarsByUserId(2L)).asList().isEmpty();
    }


    @Test
    void itShouldFindCarById() {
        assertThat(underTest.findById(carId)).isPresent();
    }

    @Test
    void itShouldNotFindCarById() {
        assertThat(underTest.findById(2L)).isNotPresent();
    }

    @Test
    void itShouldFindCarByLicensePlate() {
        assertThat(underTest.findCarByLicensePlate(licensePlate)).isPresent();
    }

    @Test
    void itShouldNotFindCarByLicensePlate() {
        assertThat(underTest.findCarByLicensePlate("TEST001")).isNotPresent();
    }
}