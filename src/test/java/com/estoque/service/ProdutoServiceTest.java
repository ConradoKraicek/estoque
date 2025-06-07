package com.estoque.service;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.ProdutoMapper;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;
    
    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveListarTodosOsProdutos() {
        // Arrange
        List<Produto> produtos = List.of(new Produto(), new Produto());
        List<ProdutoDTO> produtosDTO = List.of(
            ProdutoDTO.builder().id(1L).nome("Produto 1").build(),
            ProdutoDTO.builder().id(2L).nome("Produto 2").build()
        );
        
        when(produtoRepository.findAll()).thenReturn(produtos);
        when(produtoMapper.toDTO(any(Produto.class))).thenReturn(produtosDTO.get(0), produtosDTO.get(1));

        // Act
        List<ProdutoDTO> resultado = produtoService.listarTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(produtoRepository).findAll();
        verify(produtoMapper, times(2)).toDTO(any(Produto.class));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .id(1L)
            .nome("Produto Teste")
            .build();
            
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        // Act
        ProdutoDTO resultado = produtoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(produtoRepository).findById(1L);
        verify(produtoMapper).toDTO(produto);
    }

    @Test
    void deveSalvarProdutoComCategoriaExistente() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Teste");
        request.setPreco(100.0);
        request.setCategoriaId(1L);
        request.setEstoqueMinimo(10);
        
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        
        ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .id(1L)
            .nome("Produto Teste")
            .preco(100.0)
            .categoria(CategoriaDTO.builder().id(1L).build())
            .build();
            
        when(produtoMapper.requestToEntity(request)).thenReturn(produto);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);

        // Act
        ProdutoDTO resultado = produtoService.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals(1L, resultado.getCategoria().getId());
        verify(produtoMapper).requestToEntity(request);
        verify(categoriaRepository).findById(1L);
        verify(produtoRepository).save(produto);
        verify(produtoMapper).toDTO(produto);
    }

    @Test
    void deveAtualizarProdutoComCategoriaExistente() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Atualizado");
        request.setPreco(200.0);
        request.setCategoriaId(1L);
        request.setEstoqueMinimo(20);
        
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        
        ProdutoDTO produtoDTO = ProdutoDTO.builder()
            .id(1L)
            .nome("Produto Atualizado")
            .preco(200.0)
            .categoria(CategoriaDTO.builder().id(1L).build())
            .build();
            
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(produtoExistente)).thenReturn(produtoExistente);
        when(produtoMapper.toDTO(produtoExistente)).thenReturn(produtoDTO);

        // Act
        ProdutoDTO resultado = produtoService.atualizar(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Atualizado", resultado.getNome());
        assertEquals(1L, resultado.getCategoria().getId());
        verify(produtoRepository).findById(1L);
        verify(categoriaRepository).findById(1L);
        verify(produtoRepository).save(produtoExistente);
        verify(produtoMapper).toDTO(produtoExistente);
    }

    @Test
    void deveLancarExcecaoAoSalvarProdutoComCodigoDeBarrasDuplicado() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Teste");
        request.setCodigoBarras("123456");
        
        Produto produto = new Produto();
        produto.setCodigoBarras("123456");
        
        when(produtoMapper.requestToEntity(request)).thenReturn(produto);
        when(produtoRepository.existsByCodigoBarras("123456")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> produtoService.salvar(request));
        verify(produtoRepository).existsByCodigoBarras("123456");
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoNaoEncontrado() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Teste");
        
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizar(1L, request));
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
        Long categoriaId = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        
        List<Produto> produtos = List.of(new Produto(), new Produto());
        List<ProdutoDTO> produtosDTO = List.of(
            ProdutoDTO.builder().id(1L).nome("Produto 1").build(),
            ProdutoDTO.builder().id(2L).nome("Produto 2").build()
        );
        
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(produtoRepository.findByCategoria(categoria)).thenReturn(produtos);
        when(produtoMapper.toDTO(any(Produto.class))).thenReturn(produtosDTO.get(0), produtosDTO.get(1));

        // Act
        List<ProdutoDTO> resultado = produtoService.findByCategoria(categoriaId);

        // Assert
        assertEquals(2, resultado.size());
        verify(categoriaRepository).findById(categoriaId);
        verify(produtoRepository).findByCategoria(categoria);
        verify(produtoMapper, times(2)).toDTO(any(Produto.class));
    }
}