package com.estoque.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoriaDTOTest {

    @Test
    void deveTestarCategoriaDTO() {
        // Arrange & Act
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(1L)
                .nome("Eletrônicos")
                .descricao("Produtos eletrônicos em geral")
                .build();

        // Assert
        assertNotNull(categoriaDTO);
        assertEquals(1L, categoriaDTO.getId());
        assertEquals("Eletrônicos", categoriaDTO.getNome());
        assertEquals("Produtos eletrônicos em geral", categoriaDTO.getDescricao());

        // Testar setters
        categoriaDTO.setNome("Eletrônicos Atualizados");
        categoriaDTO.setDescricao("Descrição atualizada");

        assertEquals("Eletrônicos Atualizados", categoriaDTO.getNome());
        assertEquals("Descrição atualizada", categoriaDTO.getDescricao());
    }

    @Test
    void deveTestarCategoriaRequest() {
        // Arrange & Act
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Eletrônicos");
        request.setDescricao("Produtos eletrônicos em geral");

        // Assert
        assertNotNull(request);
        assertEquals("Eletrônicos", request.getNome());
        assertEquals("Produtos eletrônicos em geral", request.getDescricao());
    }
}
