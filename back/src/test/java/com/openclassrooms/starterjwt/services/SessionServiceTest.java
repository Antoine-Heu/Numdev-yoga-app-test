package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;
    private Teacher teacher;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setPassword("password");
        user.setAdmin(false);


        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Smith");
        teacher.setFirstName("Jane");

        session = new Session();
        session.setId(1L);
        session.setName("Yoga");
        session.setDate(new Date());
        session.setDescription("great session");
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>());
    }

    @Test
    void create_shouldSaveSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        assertEquals("great session", result.getDescription());
        assertEquals(teacher, result.getTeacher());
        assertTrue(result.getUsers().isEmpty());

        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void findAll_shouldReturnAllSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(1, result.size());
        assertEquals(session, result.get(0));
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_shouldReturnNull_whenSessionNotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(99L);

        assertNull(result);
        verify(sessionRepository).findById(99L);
    }

    @Test
    void update_shouldReplaceIdAndSave() {
        Session updatedData = new Session();
        updatedData.setName("Updated");
        updatedData.setDescription("Updated desc");

        when(sessionRepository.save(any(Session.class))).thenAnswer(inv -> inv.getArgument(0));

        Session result = sessionService.update(2L, updatedData);

        assertEquals(2L, result.getId());
        assertEquals("Updated", result.getName());
        verify(sessionRepository).save(updatedData);
    }

    @Test
    void delete_shouldCallDeleteById() {
        when(sessionRepository.existsById(1L)).thenReturn(true);

        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void participate_shouldAddUserToSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(1L, 1L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_shouldThrowNotFound_whenUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_shouldThrowBadRequest_whenAlreadyParticipating() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldRemoveUserFromSession() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldThrowNotFound_whenUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequest_whenUserNotParticipating() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
