package com.yildiz.serhat.carleaseplatform.controller.request;

import java.math.BigDecimal;

public record LeaseRateRequestDTO(
        BigDecimal mileAge,
        int duration,
        Double interestRate) {
}
