package com.yildiz.serhat.carleaseplatform.service;

import com.yildiz.serhat.carleaseplatform.controller.request.AuthenticationRequest;
import com.yildiz.serhat.carleaseplatform.controller.request.RegisterRequest;
import com.yildiz.serhat.carleaseplatform.controller.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
