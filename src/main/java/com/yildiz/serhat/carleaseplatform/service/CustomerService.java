package com.yildiz.serhat.carleaseplatform.service;

import com.yildiz.serhat.carleaseplatform.controller.request.CustomerRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;

import java.util.List;

public interface CustomerService {

    void createCustomer(CustomerRequestDTO request);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    void deleteCustomerById(Long id);

    Customer updateCustomerById(Long id, CustomerRequestDTO request);
}
