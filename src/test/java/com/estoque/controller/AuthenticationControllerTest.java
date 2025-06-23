package com.estoque.controller;

import com.estoque.dto.AuthenticationRequest;
import com.estoque.dto.AuthenticationResponse;
import com.estoque.dto.RegisterRequest;
import com.estoque.model.Role;
import com.estoque.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Usuário Teste", "usuario@teste.com", "senha123", Role.ADMIN);
        AuthenticationResponse response = new AuthenticationResponse("jwt-token-123");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<AuthenticationResponse> resultado = authenticationController.register(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("jwt-token-123", resultado.getBody().getToken());
        verify(authenticationService).register(request);
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("usuario@teste.com", "senha123");
        AuthenticationResponse response = new AuthenticationResponse("jwt-token-123");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<AuthenticationResponse> resultado = authenticationController.authenticate(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("jwt-token-123", resultado.getBody().getToken());
        verify(authenticationService).authenticate(request);
    }
}
