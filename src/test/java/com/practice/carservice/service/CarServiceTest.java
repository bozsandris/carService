package com.practice.carservice.service;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.User;
import com.practice.carservice.repo.CarRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Disabled
class CarServiceTest {

    @Mock
    private CarRepo carRepo;

    private CarService underTest;
    private Car car;

    @BeforeEach
    void setUp() {
        underTest = new CarService(carRepo);
        User user = new User("Test",  "Test", "test@test.test",
                "test", "test", "1234567", new ArrayList<>());
        car = new Car(user, "Nissan", "Micra", "TEST001", 2010);
    }

    @Test
    void canGetCarsByUserId() {
        underTest.getCarsByUserId(anyLong());

        verify(carRepo).findCarsByUserId(anyLong());
    }

    @Test
    void canGetCarById() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));

        underTest.getCarById(anyLong());

        verify(carRepo).findById(anyLong());
    }

    @Test
    void itShouldUpdateCar() {
        car.setId(1L);

        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));

        underTest.updateCar(car);

        ArgumentCaptor<Car> carArgumentCaptor =
                ArgumentCaptor.forClass(Car.class);
        verify(carRepo).save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar).isEqualTo(car);
    }

    @Test
    void willThrowWhenCarDoesNotExist() {
        car.setId(1L);

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Car with id: " + car.getId() + " does not exists");
        verify(carRepo, never()).save(any());
    }

    @Test
    void willThrowWhenBrandIsEmpty() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));
        car.setBrand("");

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("brand can't be empty");
        verify(carRepo, never()).save(any());
    }

    @Test
    void willThrowWhenModelIsEmpty() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));
        car.setModel("");

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("model can't be empty");
        verify(carRepo, never()).save(any());
    }

    @Test
    void willThrowWhenLicensePlateIsEmpty() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));
        car.setLicensePlate("");

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("license plate can't be empty");
        verify(carRepo, never()).save(any());
    }

    @Test
    void willThrowWhenLicensePlateIsTaken() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));
        given(carRepo.findCarByLicensePlate(anyString()))
                .willReturn(Optional.of(car));

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("license plate is taken");
        verify(carRepo, never()).save(any());
    }

    @Test
    void willThrowWhenProdYearIsInvalid() {
        car.setId(1L);
        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));
        car.setProdYear(0);

        assertThatThrownBy(() -> underTest.updateCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("invalid year of production");
        verify(carRepo, never()).save(any());
    }

    @Test
    void itShouldAddANewCar() {
        underTest.addNewCar(car);


        ArgumentCaptor<Car> carArgumentCaptor =
                ArgumentCaptor.forClass(Car.class);
        verify(carRepo).save(carArgumentCaptor.capture());

        Car capturedCar = carArgumentCaptor.getValue();
        assertThat(capturedCar).isEqualTo(car);
    }

    @Test
    void itWillThrowWhenLicensePlateIsTaken() {
        given(carRepo.findCarByLicensePlate(anyString()))
                .willReturn(Optional.of(car));

        assertThatThrownBy(() -> underTest.addNewCar(car))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Car with that license plate is already registered");
        verify(carRepo, never()).save(any());
    }

    @Test
    void itShouldDeleteCarById() {
        car.setId(1L);

        given(carRepo.findById(anyLong()))
                .willReturn(Optional.of(car));

        underTest.deleteCarAndRepairById(anyLong());

        verify(carRepo).delete(car);
    }
}