package com.yildiz.serhat.carleaseplatform.controller.request;

public record CustomerRequestDTO(String name,
                                 String street,
                                 String houseNumber,
                                 String zipCode,
                                 String place,
                                 String email,
                                 String phoneNumber) {
}
