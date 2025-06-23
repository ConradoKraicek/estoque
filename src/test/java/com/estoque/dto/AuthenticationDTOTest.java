package com.estoque.dto;

import com.estoque.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationDTOTest {

    @Test
    void deveTestarAuthenticationRequest() {
        // Arrange & Act
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password123");

        // Assert
        assertNotNull(request);
        assertEquals("user@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("user@example.com", request.email());
        assertEquals("password123", request.password());
    }

    @Test
    void deveTestarAuthenticationResponse() {
        // Arrange & Act
        AuthenticationResponse response = new AuthenticationResponse("jwt-token-123");

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());
        assertEquals("jwt-token-123", response.token());
    }

    @Test
    void deveTestarRegisterRequest() {
        // Arrange & Act
        RegisterRequest request = new RegisterRequest("João Silva", "joao@example.com", "senha123", Role.ADMIN);

        // Assert
        assertNotNull(request);
        assertEquals("João Silva", request.nome());
        assertEquals("joao@example.com", request.email());
        assertEquals("senha123", request.senha());
        assertEquals(Role.ADMIN, request.role());
    }
}
