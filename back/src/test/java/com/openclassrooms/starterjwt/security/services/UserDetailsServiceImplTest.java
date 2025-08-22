package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("encoded-password");
        user.setAdmin(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        assertTrue(userDetails instanceof UserDetailsImpl);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(1L, userDetailsImpl.getId());
        assertEquals("John", userDetailsImpl.getFirstName());
        assertEquals("Doe", userDetailsImpl.getLastName());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );

        assertEquals("User Not Found with email: " + email, exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldCallRepositoryWithCorrectEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userDetailsService.loadUserByUsername(email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldHandleNullEmail() {
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null)
        );
    }

    @Test
    void loadUserByUsername_shouldHandleEmptyEmail() {
        String emptyEmail = "";
        when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(emptyEmail)
        );
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserIsAdmin() {
        String email = "admin@example.com";
        User user = new User();
        user.setId(2L);
        user.setEmail(email);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword("admin-password");
        user.setAdmin(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("admin-password", userDetails.getPassword());
        assertTrue(userDetails instanceof UserDetailsImpl);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(2L, userDetailsImpl.getId());
        assertEquals("Admin", userDetailsImpl.getFirstName());
        assertEquals("User", userDetailsImpl.getLastName());
        assertNull(userDetailsImpl.getAdmin()); // admin field is not mapped in UserDetailsServiceImpl
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserHasMinimalFields() {
        String email = "minimal@example.com";
        User user = new User();
        user.setId(3L);
        user.setEmail(email);
        user.setFirstName("Min");
        user.setLastName("User");
        user.setPassword("password");
        user.setAdmin(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(3L, userDetailsImpl.getId());
        assertEquals("Min", userDetailsImpl.getFirstName());
        assertEquals("User", userDetailsImpl.getLastName());
        assertNull(userDetailsImpl.getAdmin()); // admin field is not mapped in UserDetailsServiceImpl
    }

    @Test
    void loadUserByUsername_shouldBuildCorrectUserDetailsImpl() {
        String email = "builder@example.com";
        User user = new User();
        user.setId(4L);
        user.setEmail(email);
        user.setFirstName("Builder");
        user.setLastName("Test");
        user.setPassword("builder-password");
        user.setAdmin(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        
        // Test that all UserDetailsImpl methods work correctly
        assertEquals(4L, userDetailsImpl.getId());
        assertEquals(email, userDetailsImpl.getUsername());
        assertEquals("Builder", userDetailsImpl.getFirstName());
        assertEquals("Test", userDetailsImpl.getLastName());
        assertEquals("builder-password", userDetailsImpl.getPassword());
        assertNull(userDetailsImpl.getAdmin()); // admin field is not mapped in UserDetailsServiceImpl
        
        // Test UserDetails interface methods
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void constructor_shouldInitializeCorrectly() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(mockRepository);
        
        assertNotNull(service);
        // We can't test the private field directly, but we can test that it was set
        // by calling a method that uses it
        when(mockRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        
        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("test@example.com")
        );
        
        verify(mockRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_shouldHandleRepositoryException() {
        String email = "error@example.com";
        
        when(userRepository.findByEmail(email)).thenThrow(new RuntimeException("Database error"));

        assertThrows(
                RuntimeException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );
    }
}