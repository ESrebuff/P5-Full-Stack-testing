package com.openclassrooms.starterjwt.controllers.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegisterUser_SuccessfulRegistration() throws Exception {
        // Create a JSON request body with user information
        String requestBody = "{\"email\":\"test@example.com\",\"password\":\"password\",\"firstName\":\"Test\",\"lastName\":\"Example\"}";

        // Mock the UserRepository behavior
        // - Mock that a user with the specified email doesn't exist
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // - Mock the save operation to return a new User object (simulating successful
        // registration)
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Perform an HTTP POST request to the /api/auth/register endpoint with the JSON
        // request body
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}
