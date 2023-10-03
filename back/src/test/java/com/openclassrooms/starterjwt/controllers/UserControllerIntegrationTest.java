package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:rollback.sql")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindById_UserFound_ReturnsUserDto() throws Exception {
        // Perform a GET request to retrieve user details with ID 1
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                // Authenticate as a user with the email "yoga@studio.com"
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().is(200)) // Expect a 200 (OK) response status
                .andReturn();

        // Get the content of the response as a string
        String responseContent = result.getResponse().getContentAsString();

        // Deserialize the response content into a User object using ObjectMapper
        User resultUser = objectMapper.readValue(responseContent, User.class);
        assertThat(resultUser.getFirstName()).isEqualTo("Admin");
    }

    @Test
    public void testDeleteUserById() throws Exception {
        // Perform an HTTP DELETE request to delete user with ID 2
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
                // Authenticate as a user with the email "toto@todo.com"
                .with(SecurityMockMvcRequestPostProcessors.user("toto@todo.com")))
                .andExpect(status().is(200)) // Expect a 200 (OK) response status
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

}
