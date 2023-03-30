package com.yildiz.serhat.carleaseplatform.service;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;

import java.util.List;

public interface CarService {

    void createCar(CarRequestDTO request);

    List<Car> getAllCarsByCustomer(Long customerId);

    Car getCarById(Long id);

    void deleteCarById(Long id);

    Car updateCarById(Long id, CarRequestDTO request);

}
