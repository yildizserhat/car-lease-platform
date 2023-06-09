package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.controller.request.LeaseRateRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CustomerServiceImpl customerService;
    @Mock
    private CarRepository repository;

    @Test
    void shouldCreateCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "3", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));

        when(repository.save(any())).thenReturn(Car.builder().id(3L).build());
        carService.createCar(carRequestDTO);

        verify(customerService, atLeastOnce()).getCustomerById(3L);
        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    void shouldGetAllCarsByCustomer() {
        Customer customer = Customer.builder().id(3L).build();
        when(customerService.getCustomerById(3L)).thenReturn(customer);
        when(repository.findByCustomer(customer)).thenReturn(List.of(Car.builder().customer(customer).build()));

        List<Car> allCarsByCustomer = carService.getAllCarsByCustomer(3L);

        assertEquals(allCarsByCustomer.get(0).getCustomer().getId(), 3L);
    }

    @Test
    void shouldGetCarById() {
        when(repository.findById(1L)).thenReturn(Optional.of(Car.builder().id(1L).build()));

        Car carById = carService.getCarById(1L);

        assertEquals(carById.getId(), 1L);
    }

    @Test
    void shouldUpdateCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "3", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));
        when(repository.findById(1L)).thenReturn(Optional.of(Car.builder().id(1L).build()));
        when(repository.save(any())).thenReturn(Car.buildCarFromRequest(carRequestDTO));
        Car car = carService.updateCarById(1L, carRequestDTO);

        assertEquals("BMW", car.getModel());
        assertEquals("make", car.getMake());
        assertEquals("320i", car.getVersion());
        assertEquals(new BigDecimal(40000), car.getLeaseRate().getMileAge());
    }

    @Test
    void shouldDeleteCar() {
        when(repository.findById(1L)).thenReturn(Optional.of(Car.builder().id(1L).build()));

        carService.deleteCarById(1L);

        verify(repository, atLeastOnce()).deleteById(1L);
    }

}