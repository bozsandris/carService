package com.practice.carservice.repo;

import com.practice.carservice.domain.CarPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarPartRepo extends JpaRepository<CarPart, Long> {
    Optional<CarPart> findCarPartByNameAndBrand(String name, String brand);
}
