package com.estoque.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProdutoDTOTest {

    @Test
    void deveTestarProdutoDTO() {
        // Arrange & Act
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(1L)
                .nome("Eletrônicos")
                .descricao("Produtos eletrônicos em geral")
                .build();

        ProdutoDTO produtoDTO = ProdutoDTO.builder()
                .id(1L)
                .nome("Smartphone")
                .descricao("Smartphone de última geração")
                .preco(1500.0)
                .estoqueMinimo(10)
                .categoria(categoriaDTO)
                .build();

        // Assert
        assertNotNull(produtoDTO);
        assertEquals(1L, produtoDTO.getId());
        assertEquals("Smartphone", produtoDTO.getNome());
        assertEquals("Smartphone de última geração", produtoDTO.getDescricao());
        assertEquals(1500.0, produtoDTO.getPreco());
        assertEquals(10, produtoDTO.getEstoqueMinimo());
        assertNotNull(produtoDTO.getCategoria());
        assertEquals(1L, produtoDTO.getCategoria().getId());

        // Testar setters
        produtoDTO.setNome("Smartphone Atualizado");
        produtoDTO.setPreco(1200.0);

        assertEquals("Smartphone Atualizado", produtoDTO.getNome());
        assertEquals(1200.0, produtoDTO.getPreco());
    }

    @Test
    void deveTestarProdutoRequest() {
        // Arrange & Act
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Smartphone");
        request.setDescricao("Smartphone de última geração");
        request.setPreco(1500.0);
        request.setEstoqueMinimo(10);
        request.setCategoriaId(1L);

        // Assert
        assertNotNull(request);
        assertEquals("Smartphone", request.getNome());
        assertEquals("Smartphone de última geração", request.getDescricao());
        assertEquals(1500.0, request.getPreco());
        assertEquals(10, request.getEstoqueMinimo());
        assertEquals(1L, request.getCategoriaId());
    }
}
