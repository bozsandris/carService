package com.practice.carservice.repo;

import com.practice.carservice.domain.CarPart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Disabled
class CarPartRepoTest {

    @Autowired
    private CarPartRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindCarPartByNameAndBrand() {
        CarPart tyre = new CarPart("Tyre", "brand4", 100L);
        underTest.save(tyre);

        Optional<CarPart> result = underTest.findCarPartByNameAndBrand("Tyre", "brand4");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(tyre.getName());
        assertThat(result.get().getBrand()).isEqualTo(tyre.getBrand());
    }

    @Test
    void itShouldNotFindCarPartByNameAndBrand() {
        assertThat(underTest.findCarPartByNameAndBrand("Tyre", "brand4")).isNotPresent();
    }
}