package com.practice.carservice.controller;

import com.practice.carservice.domain.CarPart;
import com.practice.carservice.service.CarPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/carpart")
@RequiredArgsConstructor
public class CarPartController {

    private final CarPartService carPartService;

    @GetMapping
    public ResponseEntity<?> getAllCarPart() {
        return ResponseEntity.ok().body(carPartService.getAllPart());
    }

    @PostMapping
    public ResponseEntity<?> addNewCarPart(@RequestBody CarPart carPart) {
        carPartService.addCarPart(carPart);
        return ResponseEntity.noContent().build();
    }
}
