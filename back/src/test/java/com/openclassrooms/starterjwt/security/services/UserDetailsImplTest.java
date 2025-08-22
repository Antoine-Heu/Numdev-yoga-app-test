package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    @Test
    void userDetailsImpl_shouldCreateWithBuilder() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("password")
                .build();

        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void userDetailsImpl_shouldCreateWithAllArgsConstructor() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@example.com",
                "John",
                "Doe",
                true,
                "password"
        );

        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void getAuthorities_shouldReturnEmptyHashSet() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
        assertEquals(0, authorities.size());
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equals_shouldReturnTrue_whenSameObject() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertEquals(userDetails, userDetails);
    }

    @Test
    void equals_shouldReturnTrue_whenSameId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test1@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password1")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("password2")
                .build();

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentId() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNull() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertNotEquals(userDetails, null);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToDifferentClass() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertNotEquals(userDetails, "string");
    }

    @Test
    void equals_shouldReturnFalse_whenIdIsNull() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(null)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void equals_shouldReturnTrue_whenBothIdsAreNull() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(null)
                .username("test1@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(null)
                .username("test2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("password")
                .build();

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void userDetailsImpl_shouldHandleNullValues() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(null)
                .username(null)
                .firstName(null)
                .lastName(null)
                .admin(null)
                .password(null)
                .build();

        assertNull(userDetails.getId());
        assertNull(userDetails.getUsername());
        assertNull(userDetails.getFirstName());
        assertNull(userDetails.getLastName());
        assertNull(userDetails.getAdmin());
        assertNull(userDetails.getPassword());
    }

    @Test
    void getAuthorities_shouldAlwaysReturnNewHashSet() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("password")
                .build();

        Collection<? extends GrantedAuthority> authorities1 = userDetails.getAuthorities();
        Collection<? extends GrantedAuthority> authorities2 = userDetails.getAuthorities();

        assertNotSame(authorities1, authorities2);
        assertEquals(authorities1.size(), authorities2.size());
    }
}