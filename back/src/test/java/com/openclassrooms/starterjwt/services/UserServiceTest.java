package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUser() {
        // GIVEN
        Long userIdToDelete = 1L;

        // Configure the mock to simulate the behavior of userRepository.deleteById
        doNothing().when(userRepository).deleteById(userIdToDelete);

        // WHEN
        // Call the delete method with the user ID to delete
        userService.delete(userIdToDelete);

        // THEN
        // Verify that the userRepository.deleteById method was called with the user ID to delete
        verify(userRepository).deleteById(userIdToDelete);
    }

    @Test
    public void testFindUserById() {
        // GIVEN
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        // Configure the mock to simulate the behavior of userRepository.findById
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(expectedUser));

        // WHEN
        // Call the findById method with the user ID
        User retrievedUser = userService.findById(userId);

        // THEN
        // Verify that the user returned by the service is the expected user
        assertThat(retrievedUser).isEqualTo(expectedUser);
    }
}
