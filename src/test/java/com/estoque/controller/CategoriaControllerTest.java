package com.estoque.controller;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.CategoriaRequest;
import com.estoque.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void deveListarTodasAsCategorias() {
        // Arrange
        Page<CategoriaDTO> categorias = new PageImpl<>(Arrays.asList(
                CategoriaDTO.builder().id(1L).nome("Eletrônicos").descricao("Produtos eletrônicos").build(),
                CategoriaDTO.builder().id(2L).nome("Móveis").descricao("Móveis para casa").build()
        ));
    
        when(categoriaService.listarTodos(any(Pageable.class))).thenReturn(categorias);
    
        // Act
        ResponseEntity<Page<CategoriaDTO>> response = categoriaController.listarTodos(PageRequest.of(0, 10));
    
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
        verify(categoriaService).listarTodos(any(Pageable.class));
    }

    @Test
    void deveBuscarCategoriaPorId() {
        // Arrange
        Long id = 1L;
        CategoriaDTO categoria = CategoriaDTO.builder()
                .id(id)
                .nome("Eletrônicos")
                .descricao("Produtos eletrônicos")
                .build();

        when(categoriaService.buscarPorId(id)).thenReturn(categoria);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.buscarPorId(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Eletrônicos", response.getBody().getNome());
        verify(categoriaService).buscarPorId(id);
    }

    @Test
    void deveSalvarCategoria() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Nova Categoria");
        request.setDescricao("Descrição da nova categoria");

        CategoriaDTO categoriaSalva = CategoriaDTO.builder()
                .id(3L)
                .nome("Nova Categoria")
                .descricao("Descrição da nova categoria")
                .build();

        when(categoriaService.salvar(any(CategoriaRequest.class))).thenReturn(categoriaSalva);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.salvar(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(3L, response.getBody().getId());
        assertEquals("Nova Categoria", response.getBody().getNome());
        verify(categoriaService).salvar(request);
    }

    @Test
    void deveAtualizarCategoria() {
        // Arrange
        Long id = 1L;
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Categoria Atualizada");
        request.setDescricao("Descrição atualizada");

        CategoriaDTO categoriaAtualizada = CategoriaDTO.builder()
                .id(id)
                .nome("Categoria Atualizada")
                .descricao("Descrição atualizada")
                .build();

        when(categoriaService.atualizar(eq(id), any(CategoriaRequest.class))).thenReturn(categoriaAtualizada);

        // Act
        ResponseEntity<CategoriaDTO> response = categoriaController.atualizar(id, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Categoria Atualizada", response.getBody().getNome());
        verify(categoriaService).atualizar(id, request);
    }

    @Test
    void deveExcluirCategoria() {
        // Arrange
        Long id = 1L;
        doNothing().when(categoriaService).excluir(id);

        // Act
        ResponseEntity<Void> response = categoriaController.excluir(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoriaService).excluir(id);
    }
}