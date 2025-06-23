package com.estoque.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Test
    void deveHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/recursos/1");

        // Act
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleResourceNotFoundException(ex, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso não encontrado", response.getBody().getMessage());
        assertEquals("uri=/api/recursos/1", response.getBody().getDetails());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void deveHandleDuplicateResourceException() {
        // Arrange
        DuplicateResourceException ex = new DuplicateResourceException("Recurso duplicado");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/recursos");

        // Act
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleDuplicateResourceException(ex, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Recurso duplicado", response.getBody().getMessage());
        assertEquals("uri=/api/recursos", response.getBody().getDetails());
        assertEquals("DUPLICATE_RESOURCE", response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }


    @Test
    void deveHandleGlobalException() {
        // Arrange
        Exception ex = new RuntimeException("Erro interno do servidor");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/recursos");

        // Act
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleGlobalException(ex, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno do servidor", response.getBody().getMessage());
        assertEquals("uri=/api/recursos", response.getBody().getDetails());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getErrorCode());
        assertNotNull(response.getBody().getTimestamp());
    }
}
