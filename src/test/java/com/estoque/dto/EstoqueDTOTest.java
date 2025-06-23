package com.estoque.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EstoqueDTOTest {

    @Test
    void deveTestarEstoqueFilialDTO() {
        // Arrange & Act
        EstoqueFilialDTO dto = EstoqueFilialDTO.builder()
                .id(1L)
                .filialId(2L)
                .filialNome("Filial Centro")
                .produtoId(3L)
                .produtoNome("Smartphone")
                .quantidade(50)
                .estoqueMinimo(10)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getFilialId());
        assertEquals("Filial Centro", dto.getFilialNome());
        assertEquals(3L, dto.getProdutoId());
        assertEquals("Smartphone", dto.getProdutoNome());
        assertEquals(50, dto.getQuantidade());
        assertEquals(10, dto.getEstoqueMinimo());

        // Testar setters
        dto.setQuantidade(45);
        dto.setEstoqueMinimo(5);

        assertEquals(45, dto.getQuantidade());
        assertEquals(5, dto.getEstoqueMinimo());
    }

    @Test
    void deveTestarMovimentacaoEstoqueDTO() {
        // Arrange & Act
        MovimentacaoEstoqueDTO dto = new MovimentacaoEstoqueDTO();
        dto.setProdutoId(1L);
        dto.setFilialId(2L);
        dto.setQuantidade(10);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getProdutoId());
        assertEquals(2L, dto.getFilialId());
        assertEquals(10, dto.getQuantidade());
    }

    @Test
    void deveTestarTransferenciaEstoqueDTO() {
        // Arrange & Act
        TransferenciaEstoqueDTO dto = new TransferenciaEstoqueDTO();
        dto.setProdutoId(1L);
        dto.setOrigemId(2L);
        dto.setDestinoId(3L);
        dto.setQuantidade(15);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getProdutoId());
        assertEquals(2L, dto.getOrigemId());
        assertEquals(3L, dto.getDestinoId());
        assertEquals(15, dto.getQuantidade());
    }
}
