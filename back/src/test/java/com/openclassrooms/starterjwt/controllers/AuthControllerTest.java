package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateUser_ValidLogin() {
        // GIVEN
        // Mock input
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock UserDetails
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock UserRepository
        User user = mock(User.class);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(user.isAdmin()).thenReturn(false);

        // Mock JWT token
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testToken");

        // WHEN
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(JwtResponse.class);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        if (jwtResponse != null) {
            assertThat(jwtResponse.getToken()).isEqualTo("testToken");
        } else {
            throw new AssertionError("jwtResponse is null");
        }
    }

    @Test
    public void testRegisterUser_SuccessfulRegistration() {
        // GIVEN
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("Example");

        // Mock UserRepository
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        // Mock PasswordEncoder
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("hashedPassword");

        // WHEN
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testRegisterUser_EmailAlreadyTaken() {
        // GIVEN
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");

        // WHEN
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
