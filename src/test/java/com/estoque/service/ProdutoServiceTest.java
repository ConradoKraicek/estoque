package com.estoque.service;

import com.estoque.exception.ResourceNotFoundException;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveSalvarProdutoComSucesso() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setPreco(4500.00);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Act
        Produto salvo = produtoService.salvar(produto);

        // Assert
        assertNotNull(salvo);
        assertEquals("Notebook", salvo.getNome());
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // Arrange
        Long idInexistente = 999L;
        when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.buscarPorId(idInexistente);
        });
    }
}
