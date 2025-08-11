package com.yildiz.serhat.carleaseplatform;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.controller.request.LeaseRateRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.repository.CarRepository;
import com.yildiz.serhat.carleaseplatform.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CarLeasePlatformApplicationTests {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Test
    void contextLoads() {
        assertNotNull(carService);
    }

    @Test
    void calculatesLeaseRate() {
        carRepository.deleteAll();

        CarRequestDTO request = new CarRequestDTO(
                "make",
                "BMW",
                "320i",
                4,
                "co2",
                new BigDecimal("50000"),
                new BigDecimal("45000"),
                "",
                new LeaseRateRequestDTO(new BigDecimal("40000"), 10, 4.5)
        );

        carService.createCar(request);

        Car saved = carRepository.findAll().get(0);
        assertEquals(new BigDecimal("169.491"), saved.getLeaseRate().getLeaseRateAmount());
    }

}
