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
        // GIVEN
        // Create a mock session with a specific values
        Session sessionToCreate = new Session();
        sessionToCreate.setName("Session de test");
        sessionToCreate.setDate(new Date());
        sessionToCreate.setDescription("Description de la session de test");

        // Configure the mock to simulate the behavior of sessionRepository.save
        when(sessionRepository.save(sessionToCreate)).thenReturn(sessionToCreate);

        // WHEN
        Session createdSession = sessionService.create(sessionToCreate);

        // THEN
        // Verify that the session is created correctly
        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getName()).isEqualTo("Session de test");
        assertThat(createdSession.getDescription()).isEqualTo("Description de la session de test");

        // Verify that the sessionRepository.save method was called with the session to
        // create
        verify(sessionRepository).save(sessionToCreate);
    }

    @Test
    public void testDeleteSession() {
        // GIVEN
        Long sessionIdToDelete = 1L;

        // Configure the mock to simulate the behavior of sessionRepository.deleteById
        doNothing().when(sessionRepository).deleteById(sessionIdToDelete);

        // WHEN
        // Call the delete method with the session ID to delete
        sessionService.delete(sessionIdToDelete);

        // THEN
        // Verify that the sessionRepository.deleteById method was called with the
        // session ID to delete
        verify(sessionRepository).deleteById(sessionIdToDelete);
    }

    @Test
    public void testFindAllSessions() {
        // GIVEN
        // Create a list of mock sessions for the findAll method
        List<Session> mockSessions = new ArrayList<>();
        mockSessions.add(new Session());
        mockSessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(mockSessions);

        // WHEN
        List<Session> retrievedSessions = sessionService.findAll();

        // THEN
        // Verify that the findAll method returned the expected list of sessions
        assertThat(retrievedSessions).isEqualTo(mockSessions);
    }

    @Test
    public void testGetSessionById() {
        // GIVEN
        // Create a mock session with a specific ID for the test
        Long sessionId = 1L;
        Session expectedSession = new Session();
        expectedSession.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        // WHEN
        // Call the getById method with the specific ID
        Session retrievedSession = sessionService.getById(sessionId);

        // THEN
        assertThat(retrievedSession).isEqualTo(expectedSession);
    }

    @Test
    public void testUpdateSession() {
        // GIVEN
        Long sessionId = 1L;
        Session sessionToUpdate = new Session();
        sessionToUpdate.setId(sessionId);
        sessionToUpdate.setName("Updated Session");

        when(sessionRepository.save(sessionToUpdate)).thenReturn(sessionToUpdate);

        // WHEN
        Session updatedSession = sessionService.update(sessionId, sessionToUpdate);

        // THEN
        assertThat(updatedSession.getId()).isEqualTo(sessionId);
        assertThat(updatedSession.getName()).isEqualTo("Updated Session");

        // Verify that the sessionRepository.save method was called with the updated
        // session
        verify(sessionRepository).save(sessionToUpdate);
    }

    @Test
    public void testParticipate_Success() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        // Configure the mock to simulate finding a session and user by ID
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // WHEN
        // Call the participate method with session and user IDs
        sessionService.participate(sessionId, userId);

        // THEN
        // Verify that the session has been updated with the added user
        assertThat(session.getUsers()).contains(user);

        // Verify that the sessionRepository.save method was called with the updated
        // session
        verify(sessionRepository).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        // Configure the mock to simulate that the session is not found
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Call the participate method with session and user IDs and expect
        // NotFoundException
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testParticipate_UserNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);

        // Configure the mock to simulate that the user is not found
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the participate method with session and user IDs and expect
        // NotFoundException
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

        // Configure the mock to simulate that the user is already participating in the
        // session
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the participate method with session and user IDs and expect
        // BadRequestException
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testNoLongerParticipate_Success() {
        // GIVEN
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

        // WHEN
        sessionService.noLongerParticipate(sessionId, userId);

        // THEN
        // Verify that the user has been removed from the session's user list
        assertThat(session.getUsers()).doesNotContain(user);
        verify(sessionRepository).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        // Configure the mock to simulate that the session is not found
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // WHEN and THEN
        // Call the noLongerParticipate method and expect a NotFoundException
        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testNoLongerParticipate_UserNotParticipating() {
        // GIVEN
        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        // Configure the mock to simulate finding the session by ID
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // WHEN and THEN
        // Call the noLongerParticipate method and expect a BadRequestException
        assertThatThrownBy(() -> sessionService.noLongerParticipate(sessionId, userId))
                .isInstanceOf(BadRequestException.class);
    }

}
