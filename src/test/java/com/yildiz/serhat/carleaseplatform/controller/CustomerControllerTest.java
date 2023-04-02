package com.yildiz.serhat.carleaseplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildiz.serhat.carleaseplatform.controller.request.CustomerRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.repository.CustomerRepository;
import com.yildiz.serhat.carleaseplatform.repository.TokenRepository;
import com.yildiz.serhat.carleaseplatform.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        objectMapper.setVisibility(FIELD, ANY);
    }

    @AfterEach
    public void tearDown() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldCreateNewCustomer() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Serhat Yildiz", "street", "44", "1062VS", "Amsterdam", "test@test.com", "650445445");
        mockMvc.perform(post("/v1/customers")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isCreated());

        List<Customer> all = customerRepository.findAll();
        Customer customer = all.get(0);

        assertEquals(customer.getFullName(), "Serhat Yildiz");
        assertEquals(customer.getStreet(), "street");
        assertEquals(customer.getHouseNumber(), "44");
        assertEquals(customer.getEmail(), "test@test.com");
        assertEquals(customer.getPhoneNumber(), "650445445");
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldGetCustomer() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Serhat Yildiz", "Street", "44", "1062VS", "Amsterdam", "test@test.com", "650445445");
        Customer customer = Customer.buildCustomerFromRequest(customerRequestDTO);
        customerRepository.save(customer);

        mockMvc.perform(get("/v1/customers/1")
                        .with(httpBasic("serhat", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Serhat Yildiz"))
                .andExpect(jsonPath("$.street").value("Street"))
                .andExpect(jsonPath("$.houseNumber").value("44"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("650445445"));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldDeleteCustomer() {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO("Serhat Yildiz", "street", "44", "1062VS", "Amsterdam", "test@test.com", "650445445");
        Customer customer = Customer.buildCustomerFromRequest(customerRequestDTO);
        customerRepository.save(customer);

        mockMvc.perform(delete("/v1/customers/1")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Customer> all = customerRepository.findAll();
        assertEquals(all.size(), 0);
    }
}