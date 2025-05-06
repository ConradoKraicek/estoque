package com.estoque.service;

import com.estoque.exception.DuplicateResourceException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveListarTodosOsProdutos() {
        // Arrange
        List<Produto> produtos = List.of(new Produto(), new Produto());
        when(produtoRepository.findAll()).thenReturn(produtos);

        // Act
        List<Produto> resultado = produtoService.listarTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(produtoRepository).findAll();
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Act
        Produto resultado = produtoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveSalvarProdutoComCategoriaExistente() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        Produto produto = new Produto();
        produto.setCategoria(categoria);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Act
        Produto salvo = produtoService.salvar(produto);

        // Assert
        assertNotNull(salvo);
        assertEquals(categoria, salvo.getCategoria());
        verify(categoriaRepository).findById(1L);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveAtualizarProdutoComCategoriaExistente() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");

        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setCategoria(categoria);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        // Act
        Produto resultado = produtoService.atualizar(1L, produtoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Atualizado", resultado.getNome());
        assertEquals(categoria, resultado.getCategoria());
        verify(produtoRepository).findById(1L);
        verify(categoriaRepository).findById(1L);
        verify(produtoRepository).save(produtoExistente);
    }

    @Test
    void deveLancarExcecaoAoSalvarProdutoComCodigoDeBarrasDuplicado() {
        // Arrange
        Produto produto = new Produto();
        produto.setCodigoBarras("123456");

        when(produtoRepository.existsByCodigoBarras("123456")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> produtoService.salvar(produto));
        verify(produtoRepository).existsByCodigoBarras("123456");
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        // Arrange
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome("Produto Atualizado");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        // Act
        Produto resultado = produtoService.atualizar(1L, produtoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Atualizado", resultado.getNome());
        verify(produtoRepository).findById(1L);
        verify(produtoRepository).save(produtoExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoNaoEncontrado() {
        // Arrange
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizar(1L, produto));
        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveExcluirProdutoComSucesso() {
        // Arrange
        Long id = 1L;

        // Act
        produtoService.excluir(id);

        // Assert
        verify(produtoRepository).deleteById(id);
    }

    @Test
    void deveBuscarProdutosPorCategoria() {
        // Arrange
        Categoria categoria = new Categoria();
        List<Produto> produtos = List.of(new Produto(), new Produto());

        when(produtoRepository.findByCategoria(categoria)).thenReturn(produtos);

        // Act
        List<Produto> resultado = produtoService.findByCategoria(categoria);

        // Assert
        assertEquals(2, resultado.size());
        verify(produtoRepository).findByCategoria(categoria);
    }
}