package com.yildiz.serhat.carleaseplatform.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record LeaseRateRequestDTO(
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 6, fraction = 0) BigDecimal mileAge,
        int duration,
        @Digits(integer = 2, fraction = 2) Double interestRate) {
}
