package com.yildiz.serhat.carleaseplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.controller.request.LeaseRateRequestDTO;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.repository.CarRepository;
import com.yildiz.serhat.carleaseplatform.repository.CustomerRepository;
import com.yildiz.serhat.carleaseplatform.repository.TokenRepository;
import com.yildiz.serhat.carleaseplatform.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    @SneakyThrows
    public void setUp() {
        Customer customer = Customer.builder()
                .id(1L)
                .email("test@test.com")
                .place("Amsterdam")
                .street("Street")
                .phoneNumber("610555444")
                .zipCode("1062VS")
                .houseNumber("55")
                .fullName("Serhat Yildiz").build();
        customerRepository.save(customer);

        objectMapper.setVisibility(FIELD, ANY);
    }


    @AfterEach
    public void tearDown() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        carRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void shouldCreateNewCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));
        mockMvc.perform(post("/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequestDTO)))
                .andExpect(status().isCreated());

        List<Car> all = carRepository.findAll();
        Car car = all.get(0);

        assertEquals("BMW", car.getModel());
        assertEquals("320i", car.getVersion());
        assertEquals(new BigDecimal(169.75), car.getLeaseRate().getLeaseRateAmount());
        assertEquals(4, car.getNumberOfDoors());
        assertEquals("test@test.com", car.getCustomer().getEmail());
        assertEquals("Serhat Yildiz", car.getCustomer().getFullName());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void shouldGetCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));

        Car car = Car.buildCarFromRequest(carRequestDTO);
        car.setId(1L);
        car.getLeaseRate().setLeaseRateAmount(new BigDecimal(169.75));
        carRepository.save(car);

        mockMvc.perform(get("/v1/cars/1")
                        .with(httpBasic("serhat", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("make"))
                .andExpect(jsonPath("$.model").value("BMW"))
                .andExpect(jsonPath("$.version").value("320i"))
                .andExpect(jsonPath("$.numberOfDoors").value(4))
                .andExpect(jsonPath("$.emission").value("co2"))
                .andExpect(jsonPath("$.leaseRate.duration").value(10));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void shouldDeleteCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));

        Car car = Car.buildCarFromRequest(carRequestDTO);
        car.getLeaseRate().setLeaseRateAmount(new BigDecimal(169.75));
        carRepository.save(car);

        mockMvc.perform(delete("/v1/cars/1")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Car> all = carRepository.findAll();

        assertEquals(0, all.size());
    }
}