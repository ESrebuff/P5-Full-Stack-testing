package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

public class TeacherControllerTest {
    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ValidId() {
        // GIVEN
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto(
                teacher.getId(),
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getCreatedAt(),
                teacher.getUpdatedAt()));

        // WHEN
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teacherService).findById(teacherId);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    public void testFindById_InvalidId() {
        // GIVEN
        String invalidId = "invalidId";

        // WHEN
        ResponseEntity<?> response = teacherController.findById(invalidId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(teacherService, never()).findById(any());
    }

    @Test
    public void testFindById_TeacherNotFound() {
        // GIVEN
        Long teacherId = 1L;

        when(teacherService.findById(teacherId)).thenReturn(null);

        // WHEN
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(teacherService).findById(teacherId);
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<Teacher> teacherList = Arrays.asList(new Teacher(), new Teacher());
    
        when(teacherService.findAll()).thenReturn(teacherList);
    
        // WHEN
        ResponseEntity<?> response = teacherController.findAll();
    
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teacherList);
    }
    
}
