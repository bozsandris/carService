package com.practice.carservice.service;

import com.practice.carservice.domain.CarPart;
import com.practice.carservice.repo.CarPartRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class CarPartService {

    private final CarPartRepo carPartRepository;

    @Autowired
    public CarPartService(CarPartRepo carPartRepository) {
        this.carPartRepository = carPartRepository;
    }

    public Collection<CarPart> getAllPart() {
        log.info("Fetching car parts");
        return carPartRepository.findAll();
    }

    public CarPart addCarPart(CarPart carPart) {
        if (carPartRepository.findCarPartByNameAndBrand(carPart.getName(), carPart.getBrand()).isPresent()) {
            throw new IllegalStateException("Car part already exists");
        }
        log.info("Saving car part {}", carPart);
        return carPartRepository.save(carPart);
    }
}
