package com.finance.service;

import com.finance.dto.LoginDto;
import com.finance.dto.RegistrationDto;
import com.finance.exception.EmailAlreadyInUseException;
import com.finance.exception.InvalidLoginException;
import com.finance.exception.UsernameAlreadyTakenException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import com.finance.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private RegistrationDto registrationDto;
    private LoginDto loginDto;

    @BeforeEach
    public void setUp() {
        registrationDto = new RegistrationDto();
        loginDto = new LoginDto();
    }

    @Test
    public void testRegisterUser_Success() {
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");

        User user = new User();

        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.registerUser(registrationDto);

        assertEquals("User registered successfully", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyTaken() {
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyTakenException.class, () -> {
            userService.registerUser(registrationDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyInUse() {
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> {
            userService.registerUser(registrationDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAuthenticateUser_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwtToken");

        String token = userService.authenticateUser(loginDto);

        assertEquals("jwtToken", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }

    @Test
    public void testAuthenticateUser_InvalidLogin() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InvalidLoginException());

        assertThrows(InvalidLoginException.class, () -> {
            userService.authenticateUser(loginDto);
        });

        verify(jwtTokenProvider, never()).generateToken(any(Authentication.class));
    }

    @Test
    public void testEncodePassword() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        String encodedPassword = userService.encodePassword("password");

        assertEquals("encodedPassword", encodedPassword);
        verify(passwordEncoder).encode("password");
    }
}

