package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.configuration.JwtService;
import com.yildiz.serhat.carleaseplatform.controller.request.AuthenticationRequest;
import com.yildiz.serhat.carleaseplatform.repository.TokenRepository;
import com.yildiz.serhat.carleaseplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticate_whenUserNotFound_throwsUsernameNotFoundException() {
        AuthenticationRequest request = new AuthenticationRequest("missing@example.com", "password");
        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.authenticate(request));
        assertEquals("Username with email missing@example.com not found", ex.getMessage());
    }
}
