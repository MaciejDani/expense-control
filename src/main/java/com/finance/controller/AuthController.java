package com.finance.controller;

import com.finance.dto.JwtAuthenticationResponseDto;
import com.finance.dto.LoginDto;
import com.finance.dto.RegistrationDto;
import com.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationDto) {
        userService.registerUser(registrationDto);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        String jwt = userService.authenticateUser(loginDto);

        return ResponseEntity.ok(new JwtAuthenticationResponseDto(jwt));

      }
}


