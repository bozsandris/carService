package com.practice.carservice;

import com.practice.carservice.controller.CarPartController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
public class WebLayerTest {

    @Autowired
    private CarPartController carPartController;

    @Test
    public void contextLoads() {
       assertThat(carPartController).isNotNull();
    }

    @Test
    public void shouldReturnDefaultMessage() {
        ResponseEntity<?> responseEntity = carPartController.getAllCarPart();
       assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(responseEntity.getBody()).asList().hasSize(6);
    }
}