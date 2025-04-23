package com.finance.controller;

import com.finance.dto.JwtAuthenticationResponseDto;
import com.finance.dto.LoginDto;
import com.finance.dto.RegistrationDto;
import com.finance.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private RegistrationDto registrationDto;
    private LoginDto loginDto;
    private String jwt;
    private String registrationResult;

    @BeforeEach
    public void setUp() {
        registrationDto = new RegistrationDto();
        loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        jwt = "test-jwt-token";
        registrationResult = "User registered successfully";
    }

    @Test
    public void testRegisterUser() {
        when(userService.registerUser(any(RegistrationDto.class))).thenReturn(registrationResult);

        ResponseEntity<?> response = authController.registerUser(registrationDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registrationResult, response.getBody());
        verify(userService).registerUser(any(RegistrationDto.class));
    }

    @Test
    public void testAuthenticateUser() {
        when(userService.authenticateUser(any(LoginDto.class))).thenReturn(jwt);
        when(userService.getDefaultCurrencyForUser(anyString())).thenReturn("PLN");

        ResponseEntity<?> response = authController.authenticateUser(loginDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new JwtAuthenticationResponseDto(jwt, "PLN"), response.getBody());
        verify(userService).authenticateUser(any(LoginDto.class));
    }
}
