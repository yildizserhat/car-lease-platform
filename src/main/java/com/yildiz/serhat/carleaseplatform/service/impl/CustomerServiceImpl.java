package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.controller.request.CustomerRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.exception.CustomerNotFoundException;
import com.yildiz.serhat.carleaseplatform.repository.CustomerRepository;
import com.yildiz.serhat.carleaseplatform.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    @Override
    public void createCustomer(CustomerRequestDTO request) {
        Customer customer = Customer.buildCustomerFromRequest(request);
        Customer savedCustomer = repository.save(customer);
        log.info("Customer saved with email: {} and Id: {}", savedCustomer.getId(), savedCustomer.getEmail());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return getCustomer(id);
    }

    @Override
    public void deleteCustomerById(Long id) {
        Customer customer = getCustomer(id);
        repository.deleteById(customer.getId());
        log.info("Customer deleted with Id: {}", customer.getId());
    }

    @Override
    public Customer updateCustomerById(Long id, CustomerRequestDTO request) {
        Customer customer = getCustomer(id);
        Customer updateCustomer = Customer.updateCustomer(customer, request);
        log.info("Customer is updated with Id: {} and email: {}", updateCustomer.getId(), updateCustomer.getEmail());
        return repository.save(updateCustomer);
    }

    private Customer getCustomer(Long id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty()) {
            String message = String.format("Customer with id:%s Not Found", id);
            log.error(message);
            throw new CustomerNotFoundException(message);
        }
        return customer.get();
    }
}
