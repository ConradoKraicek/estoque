package com.estoque.service;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.CategoriaRequest;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.CategoriaMapper;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveListarTodasAsCategorias() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        
        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNome("Eletrônicos");
        categoria1.setDescricao("Produtos eletrônicos");
        
        Categoria categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNome("Móveis");
        categoria2.setDescricao("Móveis para casa");
        
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);
        Page<Categoria> pageableCategorias = new PageImpl<>(categorias, pageable, categorias.size());
        
        CategoriaDTO categoriaDTO1 = CategoriaDTO.builder()
                .id(1L)
                .nome("Eletrônicos")
                .descricao("Produtos eletrônicos")
                .build();
        
        CategoriaDTO categoriaDTO2 = CategoriaDTO.builder()
                .id(2L)
                .nome("Móveis")
                .descricao("Móveis para casa")
                .build();
        
        when(categoriaRepository.findAll(any(Pageable.class))).thenReturn(pageableCategorias);
        when(categoriaMapper.toDTO(categoria1)).thenReturn(categoriaDTO1);
        when(categoriaMapper.toDTO(categoria2)).thenReturn(categoriaDTO2);
        
        // Act
        Page<CategoriaDTO> resultado = categoriaService.listarTodos(pageable);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals(2, resultado.getTotalElements());
        assertEquals("Eletrônicos", resultado.getContent().get(0).getNome());
        assertEquals("Móveis", resultado.getContent().get(1).getNome());
        verify(categoriaRepository).findAll(any(Pageable.class));
        verify(categoriaMapper, times(2)).toDTO(any(Categoria.class));
    }

    @Test
    void deveBuscarCategoriaPorIdComSucesso() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos");

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(1L)
                .nome("Eletrônicos")
                .descricao("Produtos eletrônicos")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDTO(categoria)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO resultado = categoriaService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Eletrônicos", resultado.getNome());
        verify(categoriaRepository).findById(1L);
        verify(categoriaMapper).toDTO(categoria);
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {
        // Arrange
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.buscarPorId(1L);
        });

        verify(categoriaRepository).findById(1L);
        verify(categoriaMapper, never()).toDTO(any(Categoria.class));
    }

    @Test
    void deveSalvarCategoriaComSucesso() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Nova Categoria");
        request.setDescricao("Descrição da nova categoria");

        Categoria categoria = new Categoria();
        categoria.setNome("Nova Categoria");
        categoria.setDescricao("Descrição da nova categoria");

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Nova Categoria");
        categoriaSalva.setDescricao("Descrição da nova categoria");

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(1L)
                .nome("Nova Categoria")
                .descricao("Descrição da nova categoria")
                .build();

        when(categoriaRepository.existsByNome("Nova Categoria")).thenReturn(false);
        when(categoriaMapper.requestToEntity(request)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(categoriaSalva);
        when(categoriaMapper.toDTO(categoriaSalva)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO resultado = categoriaService.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nova Categoria", resultado.getNome());
        verify(categoriaRepository).existsByNome("Nova Categoria");
        verify(categoriaMapper).requestToEntity(request);
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toDTO(categoriaSalva);
    }

    @Test
    void deveLancarExcecaoQuandoSalvarCategoriaComNomeDuplicado() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Categoria Existente");
        request.setDescricao("Descrição da categoria");

        when(categoriaRepository.existsByNome("Categoria Existente")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            categoriaService.salvar(request);
        });

        verify(categoriaRepository).existsByNome("Categoria Existente");
        verify(categoriaMapper, never()).requestToEntity(any());
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarCategoriaComSucesso() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Categoria Atualizada");
        request.setDescricao("Descrição atualizada");

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(1L);
        categoriaExistente.setNome("Categoria Original");
        categoriaExistente.setDescricao("Descrição original");

        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Categoria Atualizada");
        categoriaAtualizada.setDescricao("Descrição atualizada");

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(1L)
                .nome("Categoria Atualizada")
                .descricao("Descrição atualizada")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.existsByNome("Categoria Atualizada")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaAtualizada);
        when(categoriaMapper.toDTO(categoriaAtualizada)).thenReturn(categoriaDTO);

        // Act
        CategoriaDTO resultado = categoriaService.atualizar(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Categoria Atualizada", resultado.getNome());
        assertEquals("Descrição atualizada", resultado.getDescricao());
        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).existsByNome("Categoria Atualizada");
        verify(categoriaRepository).save(any(Categoria.class));
        verify(categoriaMapper).toDTO(categoriaAtualizada);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarCategoriaComNomeDuplicado() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Categoria Existente");
        request.setDescricao("Descrição atualizada");

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(1L);
        categoriaExistente.setNome("Categoria Original");
        categoriaExistente.setDescricao("Descrição original");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.existsByNome("Categoria Existente")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            categoriaService.atualizar(1L, request);
        });

        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).existsByNome("Categoria Existente");
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void deveExcluirCategoriaComSucesso() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria");
        categoria.setDescricao("Descrição");
        categoria.setProdutos(new ArrayList<>());

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act
        categoriaService.excluir(1L);

        // Assert
        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository).delete(categoria);
    }

    @Test
    void deveLancarExcecaoQuandoExcluirCategoriaComProdutosVinculados() {
        // Arrange
        List<Produto> produtos = Collections.singletonList(new Produto());
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria");
        categoria.setDescricao("Descrição");
        categoria.setProdutos(produtos);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            categoriaService.excluir(1L);
        });

        verify(categoriaRepository).findById(1L);
        verify(categoriaRepository, never()).delete(any(Categoria.class));
    }
}