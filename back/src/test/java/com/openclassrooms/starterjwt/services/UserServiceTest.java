package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

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
    }

    @Test
    void findById_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);

        assertNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void delete_shouldCallDeleteById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }
}
