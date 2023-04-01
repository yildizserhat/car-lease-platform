package com.yildiz.serhat.carleaseplatform.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO(
        @NotNull
        String name,
        @NotNull
        String street,
        @NotNull
        String houseNumber,
        @NotNull
        String zipCode,
        @NotNull
        String place,
        @NotNull
        @Email
        String email,
        @NotNull
        String phoneNumber) {
}
