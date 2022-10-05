package com.practice.carservice.service;

import com.practice.carservice.domain.Repair;
import com.practice.carservice.repo.RepairRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class RepairService {

    private final RepairRepo repairRepository;

    @Autowired
    public RepairService(RepairRepo repairRepository) {
        this.repairRepository = repairRepository;
    }

    public Collection<Repair> getRepairs() {
        log.info("Fetching repairs");

        return repairRepository.findAll();
    }

    public Repair getRepairByCarId(Long carId) {
        log.info("Fetching repair for car with id {}", carId);
        Optional<Repair> repairOptional = repairRepository.findRepairByCarId(carId);

        return repairOptional
                .orElseThrow(() -> new IllegalStateException("Repair for car with id: " + carId + " does not exists"));
    }

    public Repair addNewRepair(Repair repair) {
        log.info("Saving repair {}", repair);
        return repairRepository.save(repair);
    }

    public void updateRepair(Repair repair) {
        repairRepository.findById(repair.getId())
                .orElseThrow(() -> new IllegalStateException("Repair with id: " + repair.getId() + " does not exists."));

        log.info("Updating repair details for car with id {}", repair.getCar().getId());
        repairRepository.save(repair);
    }
}
