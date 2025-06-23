package com.estoque.mapper;

import com.estoque.dto.EstoqueFilialDTO;
import com.estoque.model.EstoqueFilial;
import com.estoque.model.Filial;
import com.estoque.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EstoqueFilialMapperTest {

    @Autowired
    private EstoqueFilialMapper estoqueFilialMapper;

    @Test
    void deveMaperarEntityParaDTO() {
        // Arrange
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial Centro");

        Produto produto = new Produto();
        produto.setId(2L);
        produto.setNome("Produto Teste");
        produto.setEstoqueMinimo(10);

        EstoqueFilial estoqueFilial = new EstoqueFilial();
        estoqueFilial.setId(1L);
        estoqueFilial.setFilial(filial);
        estoqueFilial.setProduto(produto);
        estoqueFilial.setQuantidade(50);

        // Act
        EstoqueFilialDTO dto = estoqueFilialMapper.toDTO(estoqueFilial);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getFilialId());
        assertEquals("Filial Centro", dto.getFilialNome());
        assertEquals(2L, dto.getProdutoId());
        assertEquals("Produto Teste", dto.getProdutoNome());
        assertEquals(10, dto.getEstoqueMinimo());
        assertEquals(50, dto.getQuantidade());
    }
}
