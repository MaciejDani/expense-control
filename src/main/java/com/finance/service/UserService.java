package com.finance.service;

import com.finance.dto.LoginDto;
import com.finance.dto.RegistrationDto;
import com.finance.exception.EmailAlreadyInUseException;
import com.finance.exception.InvalidLoginException;
import com.finance.exception.UsernameAlreadyTakenException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import com.finance.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String registerUser(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyTakenException();
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyInUseException();
        }
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(encodePassword(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());

        userRepository.save(user);
        return "User registered successfully";
    }

    public String authenticateUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(authentication);
        } catch (Exception ex) {
            throw new InvalidLoginException();
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}