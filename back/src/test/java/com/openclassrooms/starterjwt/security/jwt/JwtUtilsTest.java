package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String jwtSecret = "testSecret";
    private int jwtExpirationMs = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_shouldReturnValidToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@example.com",
                "John",
                "Doe",
                false,
                "password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnUsername() {
        String username = "test@example.com";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void validateJwtToken_shouldReturnTrue_whenTokenIsValid() {
        String validToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(validToken);

        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsExpired() {
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenHasInvalidSignature() {
        String invalidToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsMalformed() {
        String malformedToken = "malformed.token.here";

        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsNull() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsEmpty() {
        boolean isValid = jwtUtils.validateJwtToken("");

        assertFalse(isValid);
    }
}