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
    public void testFindById_UserNotFound_ReturnsNotFound() throws Exception {
        // Perform a GET request to retrieve user details with an invalid ID (e.g., ID
        // that doesn't exist)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/999")
                // Authenticate as a user with the email "yoga@studio.com"
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void testFindById_InvalidId_ReturnsBadRequest() throws Exception {
        // Perform a GET request with an invalid ID (non-numeric) in the path
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/invalidId")
                // Authenticate as a user with the email "yoga@studio.com"
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    public void testDeleteUserById() throws Exception {
        // Perform an HTTP DELETE request to delete user with ID 2
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
                // Authenticate as a user with the email "toto@todo.com"
                .with(SecurityMockMvcRequestPostProcessors.user("toto@todo.com")))
                .andExpect(status().is(200))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testDeleteUserById_UserNotFound_ReturnsNotFound() throws Exception {
        // Perform an HTTP DELETE request to delete a user with an ID that
        // does not exist (e.g., an invalid ID)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/999")
                // Authenticate as a user with the email "toto@todo.com"
                .with(SecurityMockMvcRequestPostProcessors.user("toto@todo.com")))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void testDeleteUserById_UnauthorizedUser_ReturnsUnauthorized() throws Exception {
        // Perform an HTTP DELETE request to delete a user (e.g., when the authenticated
        // user is not the owner of the user)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/2")
                // Authenticate as a user with the email "otheruser@example.com"
                .with(SecurityMockMvcRequestPostProcessors.user("otheruser@example.com")))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    public void testDeleteUserById_InvalidId_ReturnsBadRequest() throws Exception {
        // Perform an HTTP DELETE request with an invalid ID (e.g., a non-numeric ID) in
        // the path
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/invalidId")
                // Authenticate as a user with the email "toto@todo.com"
                .with(SecurityMockMvcRequestPostProcessors.user("toto@todo.com")))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

}
