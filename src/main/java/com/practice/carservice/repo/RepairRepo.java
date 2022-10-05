package com.practice.carservice.repo;

import com.practice.carservice.domain.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairRepo extends JpaRepository<Repair, Long> {

    Optional<Repair> findRepairByCarId(Long carId);
}
