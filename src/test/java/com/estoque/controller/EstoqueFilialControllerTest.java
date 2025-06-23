package com.estoque.controller;

import com.estoque.dto.EstoqueFilialDTO;
import com.estoque.dto.MovimentacaoEstoqueDTO;
import com.estoque.dto.TransferenciaEstoqueDTO;
import com.estoque.service.EstoqueFilialService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstoqueFilialControllerTest {

    @Mock
    private EstoqueFilialService estoqueFilialService;

    @InjectMocks
    private EstoqueFilialController estoqueFilialController;

    @Test
    void deveListarTodosOsEstoques() {
        // Arrange
        List<EstoqueFilialDTO> estoques = Arrays.asList(
                EstoqueFilialDTO.builder().id(1L).filialId(1L).produtoId(1L).quantidade(10).build(),
                EstoqueFilialDTO.builder().id(2L).filialId(1L).produtoId(2L).quantidade(20).build()
        );

        when(estoqueFilialService.listarTodos()).thenReturn(estoques);

        // Act
        ResponseEntity<List<EstoqueFilialDTO>> response = estoqueFilialController.listarTodos();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(estoqueFilialService).listarTodos();
    }

    @Test
    void deveBuscarEstoquesPorFilial() {
        // Arrange
        Long filialId = 1L;
        List<EstoqueFilialDTO> estoques = Arrays.asList(
                EstoqueFilialDTO.builder().id(1L).filialId(filialId).produtoId(1L).quantidade(10).build(),
                EstoqueFilialDTO.builder().id(2L).filialId(filialId).produtoId(2L).quantidade(20).build()
        );

        when(estoqueFilialService.buscarPorFilial(filialId)).thenReturn(estoques);

        // Act
        ResponseEntity<List<EstoqueFilialDTO>> response = estoqueFilialController.buscarPorFilial(filialId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(filialId, response.getBody().get(0).getFilialId());
        verify(estoqueFilialService).buscarPorFilial(filialId);
    }

    @Test
    void deveBuscarEstoquesPorProduto() {
        // Arrange
        Long produtoId = 1L;
        List<EstoqueFilialDTO> estoques = Arrays.asList(
                EstoqueFilialDTO.builder().id(1L).filialId(1L).produtoId(produtoId).quantidade(10).build(),
                EstoqueFilialDTO.builder().id(3L).filialId(2L).produtoId(produtoId).quantidade(15).build()
        );

        when(estoqueFilialService.buscarPorProduto(produtoId)).thenReturn(estoques);

        // Act
        ResponseEntity<List<EstoqueFilialDTO>> response = estoqueFilialController.buscarPorProduto(produtoId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(produtoId, response.getBody().get(0).getProdutoId());
        verify(estoqueFilialService).buscarPorProduto(produtoId);
    }

    @Test
    void deveAtualizarEstoque() {
        // Arrange
        Long id = 1L;
        Integer quantidade = 30;
        EstoqueFilialDTO estoqueAtualizado = EstoqueFilialDTO.builder()
                .id(id)
                .filialId(1L)
                .produtoId(1L)
                .quantidade(quantidade)
                .build();

        when(estoqueFilialService.atualizarEstoque(id, quantidade)).thenReturn(estoqueAtualizado);

        // Act
        ResponseEntity<EstoqueFilialDTO> response = estoqueFilialController.atualizarEstoque(id, quantidade);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals(quantidade, response.getBody().getQuantidade());
        verify(estoqueFilialService).atualizarEstoque(id, quantidade);
    }

    @Test
    void deveTransferirEstoque() {
        // Arrange
        TransferenciaEstoqueDTO transferencia = new TransferenciaEstoqueDTO();
        transferencia.setOrigemId(1L);
        transferencia.setDestinoId(2L);
        transferencia.setProdutoId(1L);
        transferencia.setQuantidade(5);

        doNothing().when(estoqueFilialService).transferirEstoque(any(TransferenciaEstoqueDTO.class));

        // Act
        ResponseEntity<Void> response = estoqueFilialController.transferirEstoque(transferencia);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(estoqueFilialService).transferirEstoque(transferencia);
    }

    @Test
    void deveRegistrarEntrada() {
        // Arrange
        MovimentacaoEstoqueDTO entrada = new MovimentacaoEstoqueDTO();
        entrada.setFilialId(1L);
        entrada.setProdutoId(1L);
        entrada.setQuantidade(10);

        EstoqueFilialDTO estoqueAtualizado = EstoqueFilialDTO.builder()
                .id(1L)
                .filialId(1L)
                .produtoId(1L)
                .quantidade(30) // 20 existentes + 10 de entrada
                .build();

        when(estoqueFilialService.registrarEntrada(any(MovimentacaoEstoqueDTO.class))).thenReturn(estoqueAtualizado);

        // Act
        ResponseEntity<EstoqueFilialDTO> response = estoqueFilialController.registrarEntrada(entrada);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(30, response.getBody().getQuantidade());
        verify(estoqueFilialService).registrarEntrada(entrada);
    }

    @Test
    void deveRegistrarSaida() {
        // Arrange
        MovimentacaoEstoqueDTO saida = new MovimentacaoEstoqueDTO();
        saida.setFilialId(1L);
        saida.setProdutoId(1L);
        saida.setQuantidade(5);

        EstoqueFilialDTO estoqueAtualizado = EstoqueFilialDTO.builder()
                .id(1L)
                .filialId(1L)
                .produtoId(1L)
                .quantidade(15) // 20 existentes - 5 de saída
                .build();

        when(estoqueFilialService.registrarSaida(any(MovimentacaoEstoqueDTO.class))).thenReturn(estoqueAtualizado);

        // Act
        ResponseEntity<EstoqueFilialDTO> response = estoqueFilialController.registrarSaida(saida);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15, response.getBody().getQuantidade());
        verify(estoqueFilialService).registrarSaida(saida);
    }
}
