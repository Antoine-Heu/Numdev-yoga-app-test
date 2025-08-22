package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthEntryPointJwtTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(javax.servlet.WriteListener writeListener) {
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    void commence_shouldSetUnauthorizedResponse() throws IOException, ServletException {
        String errorMessage = "Unauthorized access";
        String servletPath = "/api/secure";

        when(authException.getMessage()).thenReturn(errorMessage);
        when(request.getServletPath()).thenReturn(servletPath);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String responseContent = outputStream.toString();
        assertNotNull(responseContent);
        assertTrue(responseContent.contains("\"status\":401"));
        assertTrue(responseContent.contains("\"error\":\"Unauthorized\""));
        assertTrue(responseContent.contains("\"message\":\"" + errorMessage + "\""));
        assertTrue(responseContent.contains("\"path\":\"" + servletPath + "\""));
    }

    @Test
    void commence_shouldHandleNullMessage() throws IOException, ServletException {
        String servletPath = "/api/test";

        when(authException.getMessage()).thenReturn(null);
        when(request.getServletPath()).thenReturn(servletPath);

        authEntryPointJwt.commence(request, response, authException);

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String responseContent = outputStream.toString();
        assertNotNull(responseContent);
        assertTrue(responseContent.contains("\"status\":401"));
        assertTrue(responseContent.contains("\"error\":\"Unauthorized\""));
        assertTrue(responseContent.contains("\"message\":null"));
        assertTrue(responseContent.contains("\"path\":\"" + servletPath + "\""));
    }

    @Test
    void commence_shouldCreateValidJsonResponse() throws IOException, ServletException {
        String errorMessage = "Token expired";
        String servletPath = "/api/user";

        when(authException.getMessage()).thenReturn(errorMessage);
        when(request.getServletPath()).thenReturn(servletPath);

        authEntryPointJwt.commence(request, response, authException);

        String responseContent = outputStream.toString();
        ObjectMapper mapper = new ObjectMapper();
        
        assertDoesNotThrow(() -> {
            Map<String, Object> jsonResponse = mapper.readValue(responseContent, Map.class);
            assertEquals(401, jsonResponse.get("status"));
            assertEquals("Unauthorized", jsonResponse.get("error"));
            assertEquals(errorMessage, jsonResponse.get("message"));
            assertEquals(servletPath, jsonResponse.get("path"));
        });
    }
}