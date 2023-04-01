package com.yildiz.serhat.carleaseplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildiz.serhat.carleaseplatform.controller.request.AuthenticationRequest;
import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import com.yildiz.serhat.carleaseplatform.controller.request.LeaseRateRequestDTO;
import com.yildiz.serhat.carleaseplatform.controller.request.RegisterRequest;
import com.yildiz.serhat.carleaseplatform.controller.response.AuthenticationResponse;
import com.yildiz.serhat.carleaseplatform.domain.entity.Car;
import com.yildiz.serhat.carleaseplatform.domain.entity.Customer;
import com.yildiz.serhat.carleaseplatform.domain.entity.Role;
import com.yildiz.serhat.carleaseplatform.domain.entity.Token;
import com.yildiz.serhat.carleaseplatform.domain.entity.TokenType;
import com.yildiz.serhat.carleaseplatform.domain.entity.User;
import com.yildiz.serhat.carleaseplatform.repository.CarRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private String token = "";


    @BeforeEach
    @SneakyThrows
    public void setUp() {
        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5aWxkaXpfc2VyaGF0QGhvdG1haWwuY29tIiwiaWF0IjoxNjgwMzYyNTYyLCJleHAiOjE2ODAzNjQwMDJ9.96VeWbIqMYGE3mB8JV-lmACbqlBx5iRQd277mmD7QYM";
        User build = User.builder()
                .firstname("Serhat")
                .lastname("yildiz")
                .role(Role.USER)
                .password("123456")
                .email("email@email.com")
                .build();

        Token build1 = Token.builder()
                .user(build)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .token(token)
                .build();

        build.setTokens(List.of(build1));
        userRepository.save(build);
        tokenRepository.save(build1);



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
    @SneakyThrows
    public void shouldCreateNewCar() {

        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));
        mockMvc.perform(post("/v1/cars")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(carRequestDTO)))
                .andExpect(status().isCreated());

        List<Car> all = carRepository.findAll();
        Car car = all.get(0);

        assertEquals(car.getModel(), "BMW");
        assertEquals(car.getVersion(), "320i");
        assertEquals(car.getLeaseRate().getLeaseRate(), new BigDecimal(169.75));
        assertEquals(car.getNumberOfDoors(), 4);
        assertEquals(car.getCustomer().getEmail(), "test@test.com");
        assertEquals(car.getCustomer().getFullName(), "Serhat Yildiz");
    }

    @Test
    @SneakyThrows
    public void shouldGetCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));

        Car car = Car.buildCarFromRequest(carRequestDTO);
        car.setId(1L);
        car.getLeaseRate().setLeaseRate(new BigDecimal(169.75));
        carRepository.save(car);

        mockMvc.perform(get("/v1/cars/1")
                        .with(httpBasic("serhat", "admin"))
                        .header("Authorization", "Bearer " + token)
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
    @SneakyThrows
    public void shouldDeleteCar() {
        CarRequestDTO carRequestDTO = new CarRequestDTO("make", "BMW", "320i", 4, "co2",
                new BigDecimal(50000), new BigDecimal(45000), "1", new LeaseRateRequestDTO(new BigDecimal(40000), 10, 4.5));

        Car car = Car.buildCarFromRequest(carRequestDTO);
        car.setId(2L);
        car.getLeaseRate().setLeaseRate(new BigDecimal(169.75));
        carRepository.save(car);

        mockMvc.perform(delete("/v1/cars/2")
                        .with(httpBasic("admin", "admin"))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Car> all = carRepository.findAll();

        assertEquals(all.size(), 0);
    }

    @SneakyThrows
    private String getToken() {
        String token;
        RegisterRequest request = new RegisterRequest("serhat", "yildiz", "yildiz_serhat@hotmail.com", "1234567");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("yildiz_serhat@hotmail.com", "1234567");
        // Register and Authenticate
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/register")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn();

        AuthenticationResponse someClass = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);


        MvcResult mvcResult1 = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + someClass.getToken())
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk()).andReturn();

        AuthenticationResponse someClasss = new ObjectMapper().readValue(mvcResult1.getResponse().getContentAsString(), AuthenticationResponse.class);
        token = someClasss.getToken();
        return token;
    }

}