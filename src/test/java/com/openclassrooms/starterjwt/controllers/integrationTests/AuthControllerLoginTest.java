package com.openclassrooms.starterjwt.controllers.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticateUser_ValidLogin() throws Exception {
        String requestBody = "{\"email\":\"yoga@studio.com\",\"password\":\"test!1234\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }
}