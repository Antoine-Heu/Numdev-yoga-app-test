package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password");
        testUser.setAdmin(false);
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        try {
            userRepository.deleteAll();
        } catch (Exception e) {
            // Ignore cascade errors in tests
        }
    }

    @Test
    @WithMockUser
    void findById_shouldReturnUser_whenUserExists() throws Exception {
        mockMvc.perform(get("/api/user/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser
    void findById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void delete_shouldDeleteUser_whenUserIsOwner() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", testUser.getId()))
                .andExpect(status().isOk());

        // Verify user was deleted
        assert userRepository.findById(testUser.getId()).isEmpty();
    }

    @Test
    @WithMockUser(username = "other@example.com")
    void delete_shouldReturnUnauthorized_whenUserIsNotOwner() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", testUser.getId()))
                .andExpect(status().isUnauthorized());

        // Verify user was not deleted
        assert userRepository.findById(testUser.getId()).isPresent();
    }

    @Test
    @WithMockUser
    void delete_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void findById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void delete_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "invalid"))
                .andExpect(status().isBadRequest());
    }
}