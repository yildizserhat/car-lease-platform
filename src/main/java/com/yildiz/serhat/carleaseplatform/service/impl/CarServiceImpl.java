package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.exception.CarNotFoundException;
import com.yildiz.serhat.carleaseplatform.repository.CarRepository;
import com.yildiz.serhat.carleaseplatform.service.CarService;
import com.yildiz.serhat.carleaseplatform.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    public static final int MONTH_OF_YEAR = 12;
    private final CarRepository repository;
    private final CustomerService customerService;

    @Override
    public void createCar(CarRequestDTO request) {
        Car car = Car.buildCarFromRequest(request);
        Customer customer = null;
        if (!request.customerId().isEmpty()) {
            customer = customerService.getCustomerById(Long.valueOf(request.customerId()));
        }
        BigDecimal leaseRate = calculateLeaseRate(car);
        car.getLeaseRate().setLeaseRateAmount(leaseRate);
        car.setCustomer(customer);
        Car savedCar = repository.save(car);
        log.info("Lease Rate is calculated as :{} for carId: {}", leaseRate, savedCar.getId());
        log.info("Car saved with id:{}", savedCar.getId());
    }

    @Override
    public List<Car> getAllCarsByCustomer(Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        List<Car> customerCars = repository.findByCustomer(customer);
        if (customerCars.isEmpty()) {
            String message = String.format("Car not found for customer id:%s", customerId);
            log.error(message);
            throw new CarNotFoundException(message);
        }
        return customerCars;
    }

    @Override
    public Car getCarById(Long id) {
        return getCar(id);
    }

    @Override
    public void deleteCarById(Long id) {
        Car car = getCar(id);
        repository.deleteById(car.getId());
        log.info("Car deleted with Id: {}", car.getId());
    }

    @Override
    public Car updateCarById(Long id, CarRequestDTO request) {
        Car car = getCar(id);
        Car updatedCar = Car.updateCar(car, request);
        BigDecimal leaseRate = calculateLeaseRate(updatedCar);
        updatedCar.getLeaseRate().setLeaseRateAmount(leaseRate);
        log.info("Car is updated with Id: {}", updatedCar.getId());
        return repository.save(updatedCar);
    }

    private BigDecimal calculateLeaseRate(Car car) {
        BigDecimal mileAge = car.getLeaseRate().getMileAge();
        int duration = car.getLeaseRate().getDuration();
        BigDecimal nettPrice = car.getNettPrice();
        Double interestRate = car.getLeaseRate().getInterestRate();
        BigDecimal monthOfYear = new BigDecimal(MONTH_OF_YEAR);

        BigDecimal firstDivision = ((mileAge.divide(monthOfYear, RoundingMode.HALF_UP)).multiply(valueOf(duration))).divide(nettPrice, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
        BigDecimal secondDivision = nettPrice.divide(valueOf(12), RoundingMode.HALF_UP).multiply(valueOf(interestRate / 100)).setScale(3, RoundingMode.HALF_UP);

        return firstDivision.add(secondDivision);
    }

    private Car getCar(Long id) {
        Optional<Car> car = repository.findById(id);
        if (car.isEmpty()) {
            String message = String.format("Car with id:%s Not Found", id);
            log.error(message);
            throw new CarNotFoundException(message);
        }
        return car.get();
    }
}
