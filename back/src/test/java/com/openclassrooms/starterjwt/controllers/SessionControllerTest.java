package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findById_shouldReturnSession() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDescription("Test Description");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDescription("Test Description");

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Session"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void findById_shouldReturnNotFound_whenSessionIsNull() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnListOfSessions() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");

        when(sessionService.findAll()).thenReturn(List.of(session));
        when(sessionMapper.toDto(List.of(session))).thenReturn(List.of(sessionDto));

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Session"));
    }

    @Test
    void create_shouldCreateSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDescription("New Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        Session session = new Session();
        session.setId(1L);
        session.setName("New Session");
        session.setDescription("New Description");

        Session createdSession = new Session();
        createdSession.setId(1L);
        createdSession.setName("New Session");
        createdSession.setDescription("New Description");

        SessionDto createdSessionDto = new SessionDto();
        createdSessionDto.setId(1L);
        createdSessionDto.setName("New Session");
        createdSessionDto.setDescription("New Description");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(createdSession);
        when(sessionMapper.toDto(createdSession)).thenReturn(createdSessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Session"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    void update_shouldUpdateSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDescription("Updated Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        Session session = new Session();
        session.setName("Updated Session");
        session.setDescription("Updated Description");

        Session updatedSession = new Session();
        updatedSession.setId(1L);
        updatedSession.setName("Updated Session");
        updatedSession.setDescription("Updated Description");

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setId(1L);
        updatedSessionDto.setName("Updated Session");
        updatedSessionDto.setDescription("Updated Description");

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(updatedSession);
        when(sessionMapper.toDto(updatedSession)).thenReturn(updatedSessionDto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Session"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void update_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");

        mockMvc.perform(put("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldDeleteSession() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");

        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(sessionService).delete(1L);
    }

    @Test
    void delete_shouldReturnNotFound_whenSessionIsNull() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_shouldAddUserToSession() throws Exception {
        doNothing().when(sessionService).participate(1L, 2L);

        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_shouldReturnBadRequest_whenIdsAreInvalid() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/def"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_shouldRemoveUserFromSession() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_shouldReturnBadRequest_whenIdsAreInvalid() throws Exception {
        mockMvc.perform(delete("/api/session/abc/participate/def"))
                .andExpect(status().isBadRequest());
    }
}