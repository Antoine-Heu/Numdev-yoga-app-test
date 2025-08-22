package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityServicesIntegrationTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("integration@example.com");
        testUser.setFirstName("Integration");
        testUser.setLastName("Test");
        testUser.setPassword("encrypted-password");
        testUser.setAdmin(true);
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
    void userDetailsService_shouldLoadUserFromDatabase() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("integration@example.com");

        assertNotNull(userDetails);
        assertEquals("integration@example.com", userDetails.getUsername());
        assertEquals("encrypted-password", userDetails.getPassword());
        assertTrue(userDetails instanceof UserDetailsImpl);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(testUser.getId(), userDetailsImpl.getId());
        assertEquals("Integration", userDetailsImpl.getFirstName());
        assertEquals("Test", userDetailsImpl.getLastName());
        
        // Test all UserDetails interface methods
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void userDetailsService_shouldThrowException_whenUserNotInDatabase() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent@example.com")
        );
    }

    @Test
    void userDetailsImpl_shouldImplementEqualsCorrectly() {
        UserDetails userDetails1 = userDetailsService.loadUserByUsername("integration@example.com");
        UserDetails userDetails2 = userDetailsService.loadUserByUsername("integration@example.com");

        assertEquals(userDetails1, userDetails2);
        assertEquals(userDetails1.hashCode(), userDetails2.hashCode());
    }

    @Test
    void userDetailsService_shouldHandleDifferentUserTypes() {
        // Create a non-admin user
        User regularUser = new User();
        regularUser.setEmail("regular@example.com");
        regularUser.setFirstName("Regular");
        regularUser.setLastName("User");
        regularUser.setPassword("regular-password");
        regularUser.setAdmin(false);
        userRepository.save(regularUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("regular@example.com");

        assertNotNull(userDetails);
        assertEquals("regular@example.com", userDetails.getUsername());
        assertEquals("regular-password", userDetails.getPassword());

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals("Regular", userDetailsImpl.getFirstName());
        assertEquals("User", userDetailsImpl.getLastName());
        // admin field is not mapped, so it should be null
        assertNull(userDetailsImpl.getAdmin());
    }

    @Test
    void userDetailsImpl_shouldWorkWithBuilder() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(999L)
                .username("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .admin(true)
                .password("builder-password")
                .build();

        assertEquals(999L, userDetails.getId());
        assertEquals("builder@test.com", userDetails.getUsername());
        assertEquals("Builder", userDetails.getFirstName());
        assertEquals("Test", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("builder-password", userDetails.getPassword());

        // Test UserDetails interface methods
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void userDetailsImpl_shouldWorkWithConstructor() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                888L,
                "constructor@test.com",
                "Constructor",
                "Test",
                false,
                "constructor-password"
        );

        assertEquals(888L, userDetails.getId());
        assertEquals("constructor@test.com", userDetails.getUsername());
        assertEquals("Constructor", userDetails.getFirstName());
        assertEquals("Test", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("constructor-password", userDetails.getPassword());
    }
}