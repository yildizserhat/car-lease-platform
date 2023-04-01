package com.yildiz.serhat.carleaseplatform.repository;

import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
