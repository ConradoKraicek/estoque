package com.estoque.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExceptionClassesTest {

    @Test
    void testResourceNotFoundException() {
        // Arrange
        String errorMessage = "Recurso não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testDuplicateResourceException() {
        // Arrange
        String errorMessage = "Recurso duplicado";

        // Act
        DuplicateResourceException exception = new DuplicateResourceException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testBusinessException() {
        // Arrange
        String errorMessage = "Erro de negócio";

        // Act
        BusinessException exception = new BusinessException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}
