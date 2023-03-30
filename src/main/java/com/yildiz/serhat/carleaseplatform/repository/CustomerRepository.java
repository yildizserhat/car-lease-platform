package com.yildiz.serhat.carleaseplatform.repository;

import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
