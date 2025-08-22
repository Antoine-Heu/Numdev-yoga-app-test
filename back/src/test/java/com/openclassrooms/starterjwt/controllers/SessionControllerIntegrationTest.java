package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher teacher;
    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setPassword("password");
        user.setAdmin(false);
        user = userRepository.save(user);

        session = new Session();
        session.setName("Test Session");
        session.setDescription("Test Description");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);
    }

    @AfterEach
    void tearDown() {
        try {
            sessionRepository.deleteAll();
        } catch (Exception e) {
            // Ignore cascade errors in tests
        }
        try {
            userRepository.deleteAll();
        } catch (Exception e) {
            // Ignore cascade errors in tests
        }
        try {
            teacherRepository.deleteAll();
        } catch (Exception e) {
            // Ignore cascade errors in tests
        }
    }

    @Test
    @WithMockUser
    void findById_shouldReturnSession_whenSessionExists() throws Exception {
        mockMvc.perform(get("/api/session/{id}", session.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.name").value("Test Session"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    @WithMockUser
    void findById_shouldReturnNotFound_whenSessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void findAll_shouldReturnAllSessions() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(session.getId()))
                .andExpect(jsonPath("$[0].name").value("Test Session"));
    }

    @Test
    @WithMockUser
    void create_shouldCreateNewSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDescription("New Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Session"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    @WithMockUser
    void update_shouldUpdateExistingSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDescription("Updated Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());

        mockMvc.perform(put("/api/session/{id}", session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Session"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @WithMockUser
    void delete_shouldDeleteSession_whenSessionExists() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", session.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/{id}", session.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void participate_shouldAddUserToSession() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void noLongerParticipate_shouldRemoveUserFromSession() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", session.getId(), user.getId()))
                .andExpect(status().isOk());
    }
}