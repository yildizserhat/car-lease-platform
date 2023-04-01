package com.yildiz.serhat.carleaseplatform.controller.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CarRequestDTO(
        @NotNull
        String make,
        @NotNull
        String model,
        @NotNull
        String version,
        @NotNull
        int numberOfDoors,
        @NotNull
        String emission,
        @NotNull
        BigDecimal grossPrice,
        @NotNull
        BigDecimal nettPrice,
        @NotNull
        String customerId,
        @NotNull
        LeaseRateRequestDTO leaseRate
) {
}
