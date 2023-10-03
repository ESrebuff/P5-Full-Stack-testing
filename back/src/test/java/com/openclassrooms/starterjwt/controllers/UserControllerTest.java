package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ValidId() {
        // GIVEN
        // Mock the behavior of UserService and UserMapper
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        when(userService.findById(userId)).thenReturn(mockUser);

        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(userId);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // WHEN
        ResponseEntity<?> response = userController.findById(userId.toString());

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserDto, response.getBody());
    }

    @Test
    public void testSave_UserFoundAndAuthorized_DeletesUser() {
        // GIVEN
        UserController userController = new UserController(userService, userMapper);
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@test.com");

        // Mock the user service to return the user
        when(userService.findById(userId)).thenReturn(user);

        // Mock UserDetails and SecurityContext
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);

        // Configure mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");

        // WHEN
        ResponseEntity<?> response = userController.save(userId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).findById(userId);
        verify(userService).delete(userId);
    }
}
