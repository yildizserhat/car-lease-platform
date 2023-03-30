package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository repository;

    @Test
    public void shouldCreateCustomer() {

    }

    @Test
    public void shouldGetCustomerById() {

    }

    @Test
    public void shouldDeleteCustomer() {


    }

    @Test
    public void shouldUpdateCustomer() {

    }

}