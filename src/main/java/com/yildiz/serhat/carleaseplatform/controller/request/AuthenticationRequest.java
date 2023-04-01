package com.yildiz.serhat.carleaseplatform.controller.request;

import jakarta.validation.constraints.NotNull;


public record AuthenticationRequest(
        @NotNull String email,
        @NotNull String password) {
}
