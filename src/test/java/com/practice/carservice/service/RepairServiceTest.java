package com.practice.carservice.service;

import com.practice.carservice.domain.Car;
import com.practice.carservice.domain.Repair;
import com.practice.carservice.domain.User;
import com.practice.carservice.repo.RepairRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Disabled
class RepairServiceTest {

    @Mock
    private RepairRepo repairRepo;

    private RepairService underTest;
    private Repair repair;

    @BeforeEach
    void setUp() {
        underTest = new RepairService(repairRepo);
        User user = new User("Test", "Test", "test@test.test",
                "test", "test", "1234567", new ArrayList<>());
        Car car = new Car(user, "Nissan", "Micra", "TEST001", 2010);
        car.setId(1L);
        repair = new Repair(car, 10, List.of(), "Oil change.");
    }

    @Test
    void canGetRepairs() {
        underTest.getRepairs();

        verify(repairRepo).findAll();
    }

    @Test
    void canGetRepairByCarId() {
        given(repairRepo.findRepairByCarId(anyLong()))
                .willReturn(Optional.of(repair));

        underTest.getRepairByCarId(anyLong());

        verify(repairRepo).findRepairByCarId(anyLong());
    }

    @Test
    void itShouldAddANewRepair() {
        underTest.addNewRepair(repair);


        ArgumentCaptor<Repair> repairArgumentCaptor =
                ArgumentCaptor.forClass(Repair.class);
        verify(repairRepo).save(repairArgumentCaptor.capture());

        Repair capturedRepair = repairArgumentCaptor.getValue();
        assertThat(capturedRepair).isEqualTo(repair);
    }

    @Test
    void itShouldUpdateRepair() {
        repair.setId(1L);
        given(repairRepo.findById(anyLong()))
                .willReturn(Optional.of(repair));

        underTest.updateRepair(repair);

        ArgumentCaptor<Repair> repairArgumentCaptor =
                ArgumentCaptor.forClass(Repair.class);
        verify(repairRepo).save(repairArgumentCaptor.capture());

        Repair capturedRepair = repairArgumentCaptor.getValue();
        assertThat(capturedRepair).isEqualTo(repair);
    }

    @Test
    void itWillThrowWhenRepairDoesNotExist() {
        assertThatThrownBy(() -> underTest.updateRepair(repair))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Repair with id: " + repair.getId() + " does not exists.");
        verify(repairRepo, never()).save(any());
    }
}