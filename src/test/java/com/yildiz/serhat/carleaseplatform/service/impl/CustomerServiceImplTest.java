package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.controller.request.CustomerRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository repository;

    @Test
    void shouldCreateCustomer() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Serhat Yildiz", "street", "44", "1062VS", "Amsterdam", "test@test.com", "650445445");

        when(repository.save(any())).thenReturn(Customer.buildCustomerFromRequest(customerRequestDTO));

        customerService.createCustomer(customerRequestDTO);

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    void shouldGetCustomerById() {
        when(repository.findById(1L)).thenReturn(Optional.of(Customer.builder().id(1L).build()));

        Customer customerById = customerService.getCustomerById(1L);

        assertEquals(customerById.getId(), 1L);
    }

    @Test
    void shouldDeleteCustomer() {
        when(repository.findById(1L)).thenReturn(Optional.of(Customer.builder().id(1L).build()));

        customerService.deleteCustomerById(1L);

        verify(repository, atLeastOnce()).deleteById(1L);
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Serhat Yildiz", "street", "44", "1062VS", "Amsterdam", "test@test.com", "650445445");

        when(repository.findById(1L)).thenReturn(Optional.of(Customer.builder().id(1L).build()));
        when(repository.save(any())).thenReturn(Customer.buildCustomerFromRequest(customerRequestDTO));

        Customer customer = customerService.updateCustomerById(1L, customerRequestDTO);

        assertEquals("Serhat Yildiz", customer.getFullName());
        assertEquals("street", customer.getStreet());
        assertEquals("44", customer.getHouseNumber());
        assertEquals("1062VS", customer.getZipCode());
        assertEquals("Amsterdam", customer.getPlace());

    }

}