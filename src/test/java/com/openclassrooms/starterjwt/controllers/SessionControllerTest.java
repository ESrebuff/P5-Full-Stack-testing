package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;

public class SessionControllerTest {
    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_ValidId() {
        // GIVEN
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto());

        // WHEN
        ResponseEntity<?> response = sessionController.findById(sessionId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).getById(sessionId);
        verify(sessionMapper).toDto(session);

    }

    @Test
    public void testFindById_InvalidId() {
        // GIVEN
        String invalidId = "invalidId";

        // WHEN
        ResponseEntity<?> response = sessionController.findById(invalidId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).getById(any());
    }

    @Test
    public void testFindById_SessionNotFound() {
        // GIVEN
        Long sessionId = 1L;

        when(sessionService.getById(sessionId)).thenReturn(null);

        // WHEN
        ResponseEntity<?> response = sessionController.findById(sessionId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(sessionService).getById(sessionId);
    }

    @Test
    public void testFindAll() {
        // GIVEN
        List<Session> sessionList = Arrays.asList(new Session(), new Session());

        when(sessionService.findAll()).thenReturn(sessionList);

        // WHEN
        ResponseEntity<?> response = sessionController.findAll();

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessionList);
    }

    @Test
    public void testCreate() {
        // GIVEN
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);

        // WHEN
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).create(session);
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void testUpdate() {
        // GIVEN
        Long sessionId = 1L;
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        when(sessionService.update(any(Long.class), any(Session.class))).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);

        // WHEN
        ResponseEntity<?> response = sessionController.update(sessionId.toString(), sessionDto);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).update(any(Long.class), any(Session.class));
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionMapper).toDto(session);

    }

    @Test
    public void testSave() {
        // GIVEN
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        when(sessionService.getById(sessionId)).thenReturn(session);

        // WHEN
        ResponseEntity<?> response = sessionController.save(sessionId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).getById(sessionId);
        verify(sessionService).delete(sessionId);
    }

    @Test
    public void testParticipate() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        // WHEN
        ResponseEntity<?> response = sessionController.participate(sessionId.toString(), userId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).participate(sessionId, userId);
    }

    @Test
    public void testNoLongerParticipate() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        // WHEN
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService).noLongerParticipate(sessionId, userId);
    }

}
