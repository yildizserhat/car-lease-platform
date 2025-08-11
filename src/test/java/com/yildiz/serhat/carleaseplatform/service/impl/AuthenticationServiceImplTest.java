package com.yildiz.serhat.carleaseplatform.service.impl;

import com.yildiz.serhat.carleaseplatform.configuration.JwtService;
import com.yildiz.serhat.carleaseplatform.controller.request.AuthenticationRequest;
import com.yildiz.serhat.carleaseplatform.controller.request.RegisterRequest;
import com.yildiz.serhat.carleaseplatform.controller.response.AuthenticationResponse;
import com.yildiz.serhat.carleaseplatform.domain.Role;
import com.yildiz.serhat.carleaseplatform.domain.TokenType;
import com.yildiz.serhat.carleaseplatform.domain.entity.Token;
import com.yildiz.serhat.carleaseplatform.domain.entity.User;
import com.yildiz.serhat.carleaseplatform.repository.TokenRepository;
import com.yildiz.serhat.carleaseplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.authenticate(request));
        assertEquals("Username with email missing@example.com not found", ex.getMessage());
        // verify authenticate called with correct token
        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        UsernamePasswordAuthenticationToken authToken = authCaptor.getValue();
        assertEquals(request.email(), authToken.getPrincipal());
        assertEquals(request.password(), authToken.getCredentials());
    }

    @Test
    void authenticate_success_revokesOldTokens_savesNewToken_andReturnsJwt() {
        // Arrange
        String email = "user@example.com";
        String rawPassword = "pass";
        AuthenticationRequest request = new AuthenticationRequest(email, rawPassword);
        User user = User.builder().id(1L).email(email).password("hashed").role(Role.USER).build();
        Token old1 = Token.builder().id(10L).user(user).token("old1").tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        Token old2 = Token.builder().id(11L).user(user).token("old2").tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        String newJwt = "new-jwt-token";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(newJwt);
        when(tokenRepository.findAllValidTokenByUser(1L)).thenReturn(List.of(old1, old2));

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert response
        assertNotNull(response);
        assertEquals(newJwt, response.getToken());

        // Old tokens should be marked and saved via saveAll
        assertEquals(true, old1.isExpired());
        assertEquals(true, old1.isRevoked());
        assertEquals(true, old2.isExpired());
        assertEquals(true, old2.isRevoked());
        verify(tokenRepository).saveAll(List.of(old1, old2));

        // New token should be saved
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        Token savedToken = tokenCaptor.getValue();
        assertEquals(user, savedToken.getUser());
        assertEquals(newJwt, savedToken.getToken());
        assertEquals(TokenType.BEARER, savedToken.getTokenType());
        assertEquals(false, savedToken.isExpired());
        assertEquals(false, savedToken.isRevoked());

        // Authentication called with username/password token
        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor2 = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor2.capture());
        UsernamePasswordAuthenticationToken authToken2 = authCaptor2.getValue();
        assertEquals(email, authToken2.getPrincipal());
        assertEquals(rawPassword, authToken2.getCredentials());
    }

    @Test
    void authenticate_success_whenNoOldTokens_doesNotCallSaveAll() {
        String email = "user2@example.com";
        AuthenticationRequest request = new AuthenticationRequest(email, "pass");
        User user = User.builder().id(2L).email(email).role(Role.USER).build();
        String newJwt = "jwt-2";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(newJwt);
        when(tokenRepository.findAllValidTokenByUser(2L)).thenReturn(List.of());

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals(newJwt, response.getToken());
        verify(tokenRepository, never()).saveAll(any());
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void register_success_savesUser_encodesPassword_savesToken_andReturnsJwt() {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "secret");
        User userToSave = User.builder()
                .firstname("John").lastname("Doe").email("john@example.com").password("encoded").role(Role.USER)
                .build();
        User savedUser = User.builder()
                .id(5L).firstname("John").lastname("Doe").email("john@example.com").password("encoded").role(Role.USER)
                .build();
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-reg");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertEquals("jwt-reg", response.getToken());

        // capture saved user to ensure fields set
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User captured = userCaptor.getValue();
        assertEquals("John", captured.getFirstname());
        assertEquals("Doe", captured.getLastname());
        assertEquals("john@example.com", captured.getEmail());
        assertEquals("encoded", captured.getPassword());
        assertEquals(Role.USER, captured.getRole());

        // token saved
        verify(tokenRepository).save(any(Token.class));
    }
}
