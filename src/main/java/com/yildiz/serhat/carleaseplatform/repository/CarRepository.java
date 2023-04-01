package com.yildiz.serhat.carleaseplatform.repository;

import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByCustomer(Customer customer);
}
