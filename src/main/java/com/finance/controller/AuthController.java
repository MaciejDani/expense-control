package com.finance.controller;

import com.finance.dto.LoginDto;
import com.finance.dto.RegistrationDto;
import com.finance.dto.JwtAuthenticationResponseDto;
import com.finance.model.User;
import com.finance.security.JwtTokenProvider;
import com.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationData) {
        System.out.println("Register endpoint hit");
        if (userService.existsByUsername(registrationData.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(registrationData.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationData.getUsername());
        user.setPassword(userService.encodePassword(registrationData.getPassword()));
        user.setEmail(registrationData.getEmail());

        userService.save(user);

        System.out.println("User registered successfully");
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginData) {
        System.out.println("Login endpoint hit");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.getUsername(),
                        loginData.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        System.out.println("User authenticated successfully");
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(jwt));
    }
}


