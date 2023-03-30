package com.yildiz.serhat.carleaseplatform.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yildiz.serhat.carleaseplatform.controller.request.CustomerRequestDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String fullName;
    @NotNull
    @Column(name = "street", nullable = false)
    private String street;
    @NotNull
    @Column(name = "house_number", nullable = false)
    private String houseNumber;
    @NotNull
    @Column(name = "zip_code", nullable = false)
    private String zipCode;
    @NotNull
    @Column(name = "place", nullable = false)
    private String place;
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Car> carList = new ArrayList<>();

    public static Customer buildCustomerFromRequest(CustomerRequestDTO request) {
        return Customer.builder()
                .email(request.email())
                .fullName(request.name())
                .phoneNumber(request.phoneNumber())
                .houseNumber(request.houseNumber())
                .street(request.street())
                .zipCode(request.zipCode())
                .place(request.place())
                .active(true).build();
    }

    public static Customer updateCustomer(Customer customer, CustomerRequestDTO request) {
        customer.setEmail(request.email());
        customer.setFullName(request.name());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setHouseNumber(request.houseNumber());
        customer.setStreet(request.street());
        customer.setZipCode(request.zipCode());
        customer.setPlace(request.place());
        return customer;
    }


}
