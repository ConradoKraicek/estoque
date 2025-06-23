package com.estoque.controller;

import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.service.FilialService;
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

@ExtendWith(MockitoExtension.class)
public class FilialControllerTest {

    @Mock
    private FilialService filialService;

    @InjectMocks
    private FilialController filialController;

    @Test
    void deveListarTodasAsFiliais() {
        // Arrange
        List<FilialDTO> filiais = Arrays.asList(
                FilialDTO.builder().id(1L).nome("Filial Centro").endereco("Rua A, 123").telefone("11987654321").build(),
                FilialDTO.builder().id(2L).nome("Filial Norte").endereco("Rua B, 456").telefone("11987654322").build()
        );

        when(filialService.listarTodas()).thenReturn(filiais);

        // Act
        ResponseEntity<List<FilialDTO>> response = filialController.listarTodas();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(filialService).listarTodas();
    }

    @Test
    void deveBuscarFilialPorId() {
        // Arrange
        Long id = 1L;
        FilialDTO filial = FilialDTO.builder()
                .id(id)
                .nome("Filial Centro")
                .endereco("Rua A, 123")
                .telefone("11987654321")
                .build();

        when(filialService.buscarPorId(id)).thenReturn(filial);

        // Act
        ResponseEntity<FilialDTO> response = filialController.buscarPorId(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Filial Centro", response.getBody().getNome());
        verify(filialService).buscarPorId(id);
    }

    @Test
    void deveCriarFilial() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Nova Filial");
        request.setEndereco("Rua Nova, 789");
        request.setTelefone("11987654323");

        FilialDTO filialCriada = FilialDTO.builder()
                .id(3L)
                .nome("Nova Filial")
                .endereco("Rua Nova, 789")
                .telefone("11987654323")
                .build();

        when(filialService.criar(any(FilialRequest.class))).thenReturn(filialCriada);

        // Act
        ResponseEntity<FilialDTO> response = filialController.criar(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(3L, response.getBody().getId());
        assertEquals("Nova Filial", response.getBody().getNome());
        verify(filialService).criar(request);
    }

    @Test
    void deveAtualizarFilial() {
        // Arrange
        Long id = 1L;
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Atualizada");
        request.setEndereco("Rua Atualizada, 123");
        request.setTelefone("11987654324");

        FilialDTO filialAtualizada = FilialDTO.builder()
                .id(id)
                .nome("Filial Atualizada")
                .endereco("Rua Atualizada, 123")
                .telefone("11987654324")
                .build();

        when(filialService.atualizar(eq(id), any(FilialRequest.class))).thenReturn(filialAtualizada);

        // Act
        ResponseEntity<FilialDTO> response = filialController.atualizar(id, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Filial Atualizada", response.getBody().getNome());
        verify(filialService).atualizar(id, request);
    }

    @Test
    void deveExcluirFilial() {
        // Arrange
        Long id = 1L;
        doNothing().when(filialService).excluir(id);

        // Act
        ResponseEntity<Void> response = filialController.excluir(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(filialService).excluir(id);
    }
}
