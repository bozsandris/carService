package com.practice.carservice.service;

import com.practice.carservice.domain.CarPart;
import com.practice.carservice.repo.CarPartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Disabled
class CarPartServiceTest {

    @Mock
    private CarPartRepo carPartRepo;
    private CarPartService underTest;
    private CarPart carPart;

    @BeforeEach
    void setUp() {
        underTest = new CarPartService(carPartRepo);
        carPart = new CarPart("test", "test", 100L);
    }

    @Test
    void canGetAllPart() {
        underTest.getAllPart();

        verify(carPartRepo).findAll();
    }

    @Test
    void itShouldAddCarPart() {
        underTest.addCarPart(carPart);

        ArgumentCaptor<CarPart> carPartArgumentCaptor =
                ArgumentCaptor.forClass(CarPart.class);
        verify(carPartRepo).save(carPartArgumentCaptor.capture());

        CarPart capturedCarPart = carPartArgumentCaptor.getValue();
        assertThat(capturedCarPart).isEqualTo(carPart);
    }

    @Test
    void willThrowWhenCarPartExists() {
        given(carPartRepo.findCarPartByNameAndBrand(anyString(), anyString()))
                .willReturn(Optional.of(carPart));

        assertThatThrownBy(() -> underTest.addCarPart(carPart))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Car part already exists");
        verify(carPartRepo, never()).save(any());
    }
}