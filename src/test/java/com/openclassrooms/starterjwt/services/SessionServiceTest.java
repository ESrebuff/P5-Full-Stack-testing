package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSession() {
        Session sessionToCreate = new Session();
        sessionToCreate.setName("Session de test");
        sessionToCreate.setDate(new Date());
        sessionToCreate.setDescription("Description de la session de test");

        when(sessionRepository.save(sessionToCreate)).thenReturn(sessionToCreate);

        Session createdSession = sessionService.create(sessionToCreate);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Session de test");
        assertThat(createdSession.getDescription()).isEqualTo("Description de la session de test");

        verify(sessionRepository).save(sessionToCreate);
    }

    @Test
    public void testDeleteSession() {
        Long sessionIdToDelete = 1L;

        doNothing().when(sessionRepository).deleteById(sessionIdToDelete);

        sessionService.delete(sessionIdToDelete);

        verify(sessionRepository).deleteById(sessionIdToDelete);
    }

    @Test
    public void testFindAllSessions() {
        List<Session> mockSessions = new ArrayList<>();
        mockSessions.add(new Session());
        mockSessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(mockSessions);

        List<Session> retrievedSessions = sessionService.findAll();

        assertThat(retrievedSessions).isEqualTo(mockSessions);
    }

    @Test
    public void testGetSessionById() {
        Long sessionId = 1L;
        Session expectedSession = new Session();
        expectedSession.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        Session retrievedSession = sessionService.getById(sessionId);

        assertThat(retrievedSession).isEqualTo(expectedSession);
    }

    @Test
    public void testUpdateSession() {
        Long sessionId = 1L;
        Session sessionToUpdate = new Session();
        sessionToUpdate.setId(sessionId);
        sessionToUpdate.setName("Updated Session");

        when(sessionRepository.save(sessionToUpdate)).thenReturn(sessionToUpdate);

        Session updatedSession = sessionService.update(sessionId, sessionToUpdate);

        assertThat(updatedSession.getId()).isEqualTo(sessionId);
        assertThat(updatedSession.getName()).isEqualTo("Updated Session");

        verify(sessionRepository).save(sessionToUpdate);
    }

    @Test
    public void testParticipate_Success() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        assertThat(session.getUsers()).contains(user);

        verify(sessionRepository).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testParticipate_UserNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testParticipate_UserAlreadyParticipating() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);

        User user = new User();
        user.setId(userId);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        session.setUsers(userList);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testNoLongerParticipate_Success() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);

        User user = new User();
        user.setId(userId);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        session.setUsers(userList);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        assertThat(session.getUsers()).doesNotContain(user);
        verify(sessionRepository).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testNoLongerParticipate_UserNotParticipating() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, userId))
                .isInstanceOf(BadRequestException.class);
    }
}
