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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void listarTodosDeveRetornarListaDeCategorias() {
        List<CategoriaDTO> categorias = List.of(
            CategoriaDTO.builder().id(1L).nome("Categoria 1").build(),
            CategoriaDTO.builder().id(2L).nome("Categoria 2").build()
        );
        when(categoriaService.listarTodos()).thenReturn(categorias);

        ResponseEntity<List<CategoriaDTO>> response = categoriaController.listarTodos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(categoriaService).listarTodos();
    }

    @Test
    void buscarPorIdDeveRetornarCategoriaQuandoIdExistir() {
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
            .id(1L)
            .nome("Categoria Teste")
            .descricao("Descrição teste")
            .build();
        when(categoriaService.buscarPorId(1L)).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(categoriaService).buscarPorId(1L);
    }

    @Test
    void salvarDeveRetornarCategoriaCriada() {
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Eletrônicos");
        request.setDescricao("Produtos eletrônicos");

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
            .id(1L)
            .nome("Eletrônicos")
            .descricao("Produtos eletrônicos")
            .build();

        when(categoriaService.salvar(any(CategoriaRequest.class))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.salvar(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Eletrônicos", response.getBody().getNome());
        verify(categoriaService).salvar(any(CategoriaRequest.class));
    }

    @Test
    void atualizarDeveRetornarCategoriaAtualizada() {
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Atualizado");
        request.setDescricao("Descrição atualizada");

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
            .id(1L)
            .nome("Atualizado")
            .descricao("Descrição atualizada")
            .build();

        when(categoriaService.atualizar(eq(1L), any(CategoriaRequest.class))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.atualizar(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Atualizado", response.getBody().getNome());
        verify(categoriaService).atualizar(eq(1L), any(CategoriaRequest.class));
    }

    @Test
    void excluirDeveRetornarNoContent() {
        ResponseEntity<Void> response = categoriaController.excluir(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoriaService).excluir(1L);
    }
}
