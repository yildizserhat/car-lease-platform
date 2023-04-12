package com.yildiz.serhat.carleaseplatform.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public record AuthenticationRequest(
        @Email
        @NotNull String email,
        @NotNull String password) {
}
