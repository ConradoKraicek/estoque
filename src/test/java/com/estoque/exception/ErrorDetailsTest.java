package com.estoque.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorDetailsTest {

    @Test
    void deveCriarErrorDetailsComParametros() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        String message = "Erro de teste";
        String details = "Detalhes do erro";
        String errorCode = "CODIGO_ERRO";

        // Act
        ErrorDetails errorDetails = new ErrorDetails(timestamp, message, details, errorCode);

        // Assert
        assertNotNull(errorDetails);
        assertEquals(timestamp, errorDetails.getTimestamp());
        assertEquals(message, errorDetails.getMessage());
        assertEquals(details, errorDetails.getDetails());
        assertEquals(errorCode, errorDetails.getErrorCode());
    }

    @Test
    void devePermitirAlterarCamposViaSetters() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        LocalDateTime newTimestamp = timestamp.plusHours(1);
        ErrorDetails errorDetails = new ErrorDetails(timestamp, "Mensagem original", "Detalhes originais", "CODIGO_ORIGINAL");

        // Act
        errorDetails.setTimestamp(newTimestamp);
        errorDetails.setMessage("Mensagem alterada");
        errorDetails.setDetails("Detalhes alterados");
        errorDetails.setErrorCode("CODIGO_ALTERADO");

        // Assert
        assertEquals(newTimestamp, errorDetails.getTimestamp());
        assertEquals("Mensagem alterada", errorDetails.getMessage());
        assertEquals("Detalhes alterados", errorDetails.getDetails());
        assertEquals("CODIGO_ALTERADO", errorDetails.getErrorCode());
    }
}
