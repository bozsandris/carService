package com.practice.carservice.repo;

import com.practice.carservice.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {

    List<Car> findCarsByUserId(Long userId);

    Optional<Car> findCarByLicensePlate(String licensePlate);
}
