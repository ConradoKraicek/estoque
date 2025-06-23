package com.estoque.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilialDTOTest {

    @Test
    void deveTestarFilialDTO() {
        // Arrange & Act
        FilialDTO filialDTO = FilialDTO.builder()
                .id(1L)
                .nome("Filial Centro")
                .endereco("Rua das Flores, 123")
                .telefone("11987654321")
                .build();

        // Assert
        assertNotNull(filialDTO);
        assertEquals(1L, filialDTO.getId());
        assertEquals("Filial Centro", filialDTO.getNome());
        assertEquals("Rua das Flores, 123", filialDTO.getEndereco());
        assertEquals("11987654321", filialDTO.getTelefone());

        // Testar setters
        filialDTO.setNome("Filial Centro Atualizada");
        filialDTO.setEndereco("Av. Paulista, 1000");

        assertEquals("Filial Centro Atualizada", filialDTO.getNome());
        assertEquals("Av. Paulista, 1000", filialDTO.getEndereco());
    }

    @Test
    void deveTestarFilialRequest() {
        // Arrange & Act
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Centro");
        request.setEndereco("Rua das Flores, 123");
        request.setTelefone("11987654321");

        // Assert
        assertNotNull(request);
        assertEquals("Filial Centro", request.getNome());
        assertEquals("Rua das Flores, 123", request.getEndereco());
        assertEquals("11987654321", request.getTelefone());
    }
}
