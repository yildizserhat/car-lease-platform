package com.yildiz.serhat.carleaseplatform.controller;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/cars")
@RequiredArgsConstructor
@Tag(name = "Car", description = "Endpoints about cars")
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Create Car By id")
    public ResponseEntity<Void> createCar(@RequestBody @Valid CarRequestDTO request) {
        carService.createCar(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get All Car")
    public ResponseEntity<List<Car>> getAllCars(@PathVariable("id") Long id) {
        return new ResponseEntity<>(carService.getAllCarsByCustomer(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Car By id")
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(carService.getCarById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Car By id")
    public ResponseEntity<Void> deleteCarById(@PathVariable("id") Long id) {
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Car By id")
    public ResponseEntity<Car> updateCarById(@PathVariable("id") Long id, @RequestBody @Valid CarRequestDTO request) {
        return new ResponseEntity<>(carService.updateCarById(id, request), HttpStatus.OK);
    }
}
