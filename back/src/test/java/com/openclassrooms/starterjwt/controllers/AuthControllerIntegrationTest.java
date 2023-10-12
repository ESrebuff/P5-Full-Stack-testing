package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.payload.response.JwtResponse;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:rollback.sql")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticateUser_ValidLogin() throws Exception {
        // Create a JSON request body with user information
        String requestBody = "{\"email\":\"yoga@studio.com\",\"password\":\"test!1234\"}";

        // Perform an HTTP POST request to the /api/auth/register endpoint with the JSON
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Create a JSON request body with user information
        String requestBody = "{\"email\":\"test@example.com\",\"password\":\"password\",\"firstName\":\"Test\",\"lastName\":\"Example\"}";

        // Perform an HTTP POST request to the /api/auth/register endpoint with the JSON
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testRegisterUser_EmailAlreadyTaken() throws Exception {
        // Create a JSON request body with an email that is already in the database
        String requestBody = "{\"email\":\"yoga@studio.com\",\"password\":\"password\",\"firstName\":\"Test\",\"lastName\":\"Example\"}";

        // Perform an HTTP POST request to the /api/auth/register endpoint with the JSON
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting a bad request
                .andReturn();

        // Check if the response body contains the expected error message
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).contains("Error: Email is already taken!");
    }

}