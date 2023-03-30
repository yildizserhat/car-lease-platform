package com.yildiz.serhat.carleaseplatform.controller.request;

import java.math.BigDecimal;

public record CarRequestDTO(String make,
                            String model,
                            String version,
                            int numberOfDoors,
                            String emission,
                            BigDecimal grossPrice,
                            BigDecimal nettPrice,
                            String customerId,
                            LeaseRateRequestDTO leaseRate
) {
}
