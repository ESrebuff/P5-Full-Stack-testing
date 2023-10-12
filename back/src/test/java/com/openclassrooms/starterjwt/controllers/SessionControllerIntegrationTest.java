package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:rollback.sql")
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindSessionById_SessionExists_ReturnsSessionDto() throws Exception {
        // Perform an HTTP GET request to retrieve session details with a valid ID
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testFindAllSessions() throws Exception {
        // Perform an HTTP GET request to retrieve all sessions
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

    }

    @Test
    public void testFindSessionById_SessionNotFound_ReturnsNotFound() throws Exception {
        // Perform an HTTP GET request to retrieve a session with an ID that does not
        // exist
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/session/999")
                        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isNotFound()) // Expect a 404 (Not Found) response status
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void testCreateSession_ValidData_ReturnsSessionDto() throws Exception {
        // Create a JSON request body with valid session data
        String requestBody = "{\"name\":\"Session Name\",\"description\":\"Description\",\"date\":\"2023-10-15\",\"teacher_id\":\"1\"}";

        // Perform an HTTP POST request to create a new session
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk()) // Expect a 200 (OK) response status
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        // Add assertions to validate the response content
    }

    @Test
    public void testCreateSession_InvalidData_ReturnsBadRequest() throws Exception {
        // Create a JSON request body with invalid session data
        String requestBody = "{\"description\":\"Description\",\"date\":\"InvalidDate\"}";

        // Perform an HTTP POST request to create a new session with invalid data
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest()) // Expect a 400 (Bad Request) response status
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    public void testUpdateSession_ValidInput() throws Exception {
        // Create a JSON request body with valid session data for updating
        String requestBody = "{\"name\":\"Updated Name\",\"description\":\"Updated Description\",\"date\":\"2023-10-16\",\"teacher_id\":\"1\"}";

        // Perform an HTTP PUT request to update a session
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testUpdateSession_InvalidInput_ReturnsBadRequest() throws Exception {
        // Create a JSON request body with invalid session data for updating
        String requestBody = "{\"description\":\"Description\",\"date\":\"InvalidDate\"}";

        // Perform an HTTP PUT request to update a session with invalid data
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    public void testDeleteSession_ExistingSession() throws Exception {
        // Perform an HTTP DELETE request to delete an existing session
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testDeleteSession_NonExistingSession_ReturnsNotFound() throws Exception {
        // Perform an HTTP DELETE request to delete a non-existing session
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/999")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void testParticipateInSession_ValidInput() throws Exception {
        // Perform an HTTP POST request to participate in a session
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testParticipateInSession_InvalidInput_ReturnsBadRequest() throws Exception {
        // Perform an HTTP POST request to participate in a session with invalid input
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/string/participate/2")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    public void testNoLongerParticipate_ValidInput() throws Exception {
        // Perform an HTTP DELETE request to no longer participate in a session
        MvcResult participateResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/session/1/participate/2")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(participateResult.getResponse().getStatus()).isEqualTo(200);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/1/participate/2")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testNoLongerParticipate_InvalidInput_ReturnsBadRequest() throws Exception {
        // Perform an HTTP DELETE request to no longer participate in a session with
        // invalid input
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/string/participate/2")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

}
