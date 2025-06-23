package com.estoque.service;

import com.estoque.exception.ResourceNotFoundException;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RelatorioProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private RelatorioProdutoService relatorioProdutoService;

    @Test
    void deveGerarRelatorioDeTodosProdutos() throws Exception {
        // Arrange
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Produto 1");
        produto1.setDescricao("Descrição 1");
        produto1.setPreco(10.0);
        produto1.setEstoqueMinimo(2);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Produto 2");
        produto2.setDescricao("Descrição 2");
        produto2.setPreco(20.0);
        produto2.setEstoqueMinimo(3);

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(produtoRepository.findAll()).thenReturn(produtos);

        // Act
        byte[] resultado = relatorioProdutoService.gerarRelatorioProdutosPdf(null);

        // Assert
        assertNotNull(resultado);
        verify(produtoRepository).findAll();
        verify(categoriaRepository, never()).findById(any());
    }

    @Test
    void deveGerarRelatorioDeProdutosFiltradoPorCategoria() throws Exception {
        // Arrange
        Long categoriaId = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos");

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Smartphone");
        produto1.setDescricao("Smartphone de última geração");
        produto1.setPreco(1500.0);
        produto1.setCategoria(categoria);
        produto1.setEstoqueMinimo(2);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tablet");
        produto2.setDescricao("Tablet premium");
        produto2.setPreco(2000.0);
        produto2.setCategoria(categoria);
        produto2.setEstoqueMinimo(1);

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(produtoRepository.findByCategoria(categoria)).thenReturn(produtos);

        // Act
        byte[] resultado = relatorioProdutoService.gerarRelatorioProdutosPdf(categoriaId);

        // Assert
        assertNotNull(resultado);
        verify(categoriaRepository).findById(categoriaId);
        verify(produtoRepository).findByCategoria(categoria);
        verify(produtoRepository, never()).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaInexistente() {
        // Arrange
        Long categoriaId = 999L;

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            relatorioProdutoService.gerarRelatorioProdutosPdf(categoriaId);
        });

        verify(categoriaRepository).findById(categoriaId);
        verify(produtoRepository, never()).findByCategoria(any());
        verify(produtoRepository, never()).findAll();
    }
}
