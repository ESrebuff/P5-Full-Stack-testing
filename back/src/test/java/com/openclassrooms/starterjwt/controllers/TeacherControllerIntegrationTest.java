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
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindTeacherById_TeacherExists_ReturnsTeacherDto() throws Exception {
        // Perform an HTTP GET request to retrieve teacher details with a valid ID
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/1")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testFindAllTeachers() throws Exception {
        // Perform an HTTP GET request to retrieve all teachers
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    public void testFindTeacherById_TeacherNotFound_ReturnsNotFound() throws Exception {
        // Perform an HTTP GET request to retrieve a teacher with an ID that does not exist
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/999")
                .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
                .andExpect(status().isNotFound()) // Expect a 404 (Not Found) response status
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }
}
