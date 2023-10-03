package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllTeachers() {
        // GIVEN
        // Create a list of mock teachers for the findAll method
        List<Teacher> mockTeachers = new ArrayList<>();
        mockTeachers.add(new Teacher());
        mockTeachers.add(new Teacher());

        // Configure the mock to return the list of mock teachers when findAll is called
        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        // WHEN
        List<Teacher> retrievedTeachers = teacherService.findAll();

        // THEN
        // Verify that the findAll method returned the expected list of teachers
        assertThat(retrievedTeachers).isEqualTo(mockTeachers);
    }

    @Test
    public void testGetTeacherById() {
        // GIVEN
        Long teacherId = 1L;
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

        // WHEN
        // Call the findById method with the specific ID
        Teacher retrivedTeacher = teacherService.findById(teacherId);

        // THEN
        assertThat(retrivedTeacher).isEqualTo(expectedTeacher);
    }

    @Test
    public void testGetTeacherById_TeacherNotFound() {
        // GIVEN
        Long teacherId = 1L;
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

        // WHEN
        // Call the findById method with the specific ID
        Teacher retrivedTeacher = teacherService.findById(2L);

        // THEN
        assertThat(retrivedTeacher).isNotEqualTo(expectedTeacher);
    }

}
