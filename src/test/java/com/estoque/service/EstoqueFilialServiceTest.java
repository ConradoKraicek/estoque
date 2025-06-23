package com.estoque.service;

import com.estoque.dto.EstoqueFilialDTO;
import com.estoque.dto.MovimentacaoEstoqueDTO;
import com.estoque.dto.TransferenciaEstoqueDTO;
import com.estoque.exception.BusinessException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.EstoqueFilialMapper;
import com.estoque.model.EstoqueFilial;
import com.estoque.model.Filial;
import com.estoque.model.Produto;
import com.estoque.repository.EstoqueFilialRepository;
import com.estoque.repository.FilialRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstoqueFilialServiceTest {

    @Mock
    private EstoqueFilialRepository estoqueFilialRepository;

    @Mock
    private FilialRepository filialRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EstoqueFilialMapper estoqueFilialMapper;

    @InjectMocks
    private EstoqueFilialService estoqueFilialService;

    @Test
    void deveListarTodosOsEstoques() {
        // Arrange
        EstoqueFilial estoque1 = EstoqueFilial.builder().id(1L).quantidade(10).build();
        EstoqueFilial estoque2 = EstoqueFilial.builder().id(2L).quantidade(20).build();
        List<EstoqueFilial> estoques = Arrays.asList(estoque1, estoque2);

        EstoqueFilialDTO dto1 = EstoqueFilialDTO.builder().id(1L).quantidade(10).build();
        EstoqueFilialDTO dto2 = EstoqueFilialDTO.builder().id(2L).quantidade(20).build();

        when(estoqueFilialRepository.findAll()).thenReturn(estoques);
        when(estoqueFilialMapper.toDTO(estoque1)).thenReturn(dto1);
        when(estoqueFilialMapper.toDTO(estoque2)).thenReturn(dto2);

        // Act
        List<EstoqueFilialDTO> resultado = estoqueFilialService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estoqueFilialRepository).findAll();
        verify(estoqueFilialMapper, times(2)).toDTO(any(EstoqueFilial.class));
    }

    @Test
    void deveBuscarEstoquesPorFilial() {
        // Arrange
        Long filialId = 1L;
        EstoqueFilial estoque1 = EstoqueFilial.builder().id(1L).quantidade(10).build();
        EstoqueFilial estoque2 = EstoqueFilial.builder().id(2L).quantidade(20).build();
        List<EstoqueFilial> estoques = Arrays.asList(estoque1, estoque2);

        EstoqueFilialDTO dto1 = EstoqueFilialDTO.builder().id(1L).quantidade(10).build();
        EstoqueFilialDTO dto2 = EstoqueFilialDTO.builder().id(2L).quantidade(20).build();

        when(estoqueFilialRepository.findByFilialId(filialId)).thenReturn(estoques);
        when(estoqueFilialMapper.toDTO(estoque1)).thenReturn(dto1);
        when(estoqueFilialMapper.toDTO(estoque2)).thenReturn(dto2);

        // Act
        List<EstoqueFilialDTO> resultado = estoqueFilialService.buscarPorFilial(filialId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estoqueFilialRepository).findByFilialId(filialId);
        verify(estoqueFilialMapper, times(2)).toDTO(any(EstoqueFilial.class));
    }

    @Test
    void deveBuscarEstoquesPorProduto() {
        // Arrange
        Long produtoId = 1L;
        EstoqueFilial estoque1 = EstoqueFilial.builder().id(1L).quantidade(10).build();
        EstoqueFilial estoque2 = EstoqueFilial.builder().id(2L).quantidade(20).build();
        List<EstoqueFilial> estoques = Arrays.asList(estoque1, estoque2);

        EstoqueFilialDTO dto1 = EstoqueFilialDTO.builder().id(1L).quantidade(10).build();
        EstoqueFilialDTO dto2 = EstoqueFilialDTO.builder().id(2L).quantidade(20).build();

        when(estoqueFilialRepository.findByProdutoId(produtoId)).thenReturn(estoques);
        when(estoqueFilialMapper.toDTO(estoque1)).thenReturn(dto1);
        when(estoqueFilialMapper.toDTO(estoque2)).thenReturn(dto2);

        // Act
        List<EstoqueFilialDTO> resultado = estoqueFilialService.buscarPorProduto(produtoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estoqueFilialRepository).findByProdutoId(produtoId);
        verify(estoqueFilialMapper, times(2)).toDTO(any(EstoqueFilial.class));
    }

    @Test
    void deveAtualizarEstoqueComSucesso() {
        // Arrange
        Long estoqueId = 1L;
        Integer novaQuantidade = 50;

        EstoqueFilial estoque = EstoqueFilial.builder()
                .id(estoqueId)
                .quantidade(30)
                .build();

        EstoqueFilial estoqueAtualizado = EstoqueFilial.builder()
                .id(estoqueId)
                .quantidade(novaQuantidade)
                .build();

        EstoqueFilialDTO estoqueDTO = EstoqueFilialDTO.builder()
                .id(estoqueId)
                .quantidade(novaQuantidade)
                .build();

        when(estoqueFilialRepository.findById(estoqueId)).thenReturn(Optional.of(estoque));
        when(estoqueFilialRepository.save(any(EstoqueFilial.class))).thenReturn(estoqueAtualizado);
        when(estoqueFilialMapper.toDTO(estoqueAtualizado)).thenReturn(estoqueDTO);

        // Act
        EstoqueFilialDTO resultado = estoqueFilialService.atualizarEstoque(estoqueId, novaQuantidade);

        // Assert
        assertNotNull(resultado);
        assertEquals(estoqueId, resultado.getId());
        assertEquals(novaQuantidade, resultado.getQuantidade());
        verify(estoqueFilialRepository).findById(estoqueId);
        verify(estoqueFilialRepository).save(any(EstoqueFilial.class));
        verify(estoqueFilialMapper).toDTO(estoqueAtualizado);
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueNaoEncontradoAoAtualizar() {
        // Arrange
        Long estoqueId = 999L;
        Integer novaQuantidade = 50;

        when(estoqueFilialRepository.findById(estoqueId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            estoqueFilialService.atualizarEstoque(estoqueId, novaQuantidade);
        });

        verify(estoqueFilialRepository).findById(estoqueId);
        verify(estoqueFilialRepository, never()).save(any(EstoqueFilial.class));
        verify(estoqueFilialMapper, never()).toDTO(any(EstoqueFilial.class));
    }

    @Test
    void deveTransferirEstoqueComSucesso() {
        // Arrange
        TransferenciaEstoqueDTO transferencia = new TransferenciaEstoqueDTO();
        transferencia.setOrigemId(1L);
        transferencia.setDestinoId(2L);
        transferencia.setProdutoId(1L);
        transferencia.setQuantidade(10);

        Filial filialOrigem = new Filial();
        filialOrigem.setId(1L);
        Filial filialDestino = new Filial();
        filialDestino.setId(2L);
        Produto produto = new Produto();
        produto.setId(1L);

        EstoqueFilial estoqueOrigem = EstoqueFilial.builder()
                .id(1L)
                .filial(filialOrigem)
                .produto(produto)
                .quantidade(20)
                .build();

        EstoqueFilial estoqueDestino = EstoqueFilial.builder()
                .id(2L)
                .filial(filialDestino)
                .produto(produto)
                .quantidade(5)
                .build();

        when(estoqueFilialRepository.findByFilialIdAndProdutoId(1L, 1L)).thenReturn(Optional.of(estoqueOrigem));
        when(estoqueFilialRepository.findByFilialIdAndProdutoId(2L, 1L)).thenReturn(Optional.of(estoqueDestino));

        // Act
        estoqueFilialService.transferirEstoque(transferencia);

        // Assert
        assertEquals(10, estoqueOrigem.getQuantidade()); // 20 - 10
        assertEquals(15, estoqueDestino.getQuantidade()); // 5 + 10
        verify(estoqueFilialRepository).findByFilialIdAndProdutoId(1L, 1L);
        verify(estoqueFilialRepository).findByFilialIdAndProdutoId(2L, 1L);
        verify(estoqueFilialRepository).save(estoqueOrigem);
        verify(estoqueFilialRepository).save(estoqueDestino);
    }

    @Test
    void deveLancarExcecaoQuandoTransferirParaMesmaFilial() {
        // Arrange
        TransferenciaEstoqueDTO transferencia = new TransferenciaEstoqueDTO();
        transferencia.setOrigemId(1L);
        transferencia.setDestinoId(1L); // Mesma filial
        transferencia.setProdutoId(1L);
        transferencia.setQuantidade(10);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            estoqueFilialService.transferirEstoque(transferencia);
        });

        verify(estoqueFilialRepository, never()).findByFilialIdAndProdutoId(anyLong(), anyLong());
        verify(estoqueFilialRepository, never()).save(any(EstoqueFilial.class));
    }

    @Test
    void deveRegistrarEntradaDeEstoqueComSucesso() {
        // Arrange
        MovimentacaoEstoqueDTO entrada = new MovimentacaoEstoqueDTO();
        entrada.setFilialId(1L);
        entrada.setProdutoId(1L);
        entrada.setQuantidade(15);

        Filial filial = new Filial();
        filial.setId(1L);
        Produto produto = new Produto();
        produto.setId(1L);

        EstoqueFilial estoqueExistente = EstoqueFilial.builder()
                .id(1L)
                .filial(filial)
                .produto(produto)
                .quantidade(10)
                .build();

        EstoqueFilial estoqueAtualizado = EstoqueFilial.builder()
                .id(1L)
                .filial(filial)
                .produto(produto)
                .quantidade(25) // 10 + 15
                .build();

        EstoqueFilialDTO estoqueDTO = EstoqueFilialDTO.builder()
                .id(1L)
                .filialId(1L)
                .produtoId(1L)
                .quantidade(25)
                .build();

        when(estoqueFilialRepository.findByFilialIdAndProdutoId(1L, 1L)).thenReturn(Optional.of(estoqueExistente));
        when(estoqueFilialRepository.save(any(EstoqueFilial.class))).thenReturn(estoqueAtualizado);
        when(estoqueFilialMapper.toDTO(estoqueAtualizado)).thenReturn(estoqueDTO);

        // Act
        EstoqueFilialDTO resultado = estoqueFilialService.registrarEntrada(entrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(25, resultado.getQuantidade());
        verify(estoqueFilialRepository).findByFilialIdAndProdutoId(1L, 1L);
        verify(estoqueFilialRepository).save(any(EstoqueFilial.class));
        verify(estoqueFilialMapper).toDTO(estoqueAtualizado);
    }

    @Test
    void deveRegistrarSaidaDeEstoqueComSucesso() {
        // Arrange
        MovimentacaoEstoqueDTO saida = new MovimentacaoEstoqueDTO();
        saida.setFilialId(1L);
        saida.setProdutoId(1L);
        saida.setQuantidade(5);

        Filial filial = new Filial();
        filial.setId(1L);
        Produto produto = new Produto();
        produto.setId(1L);

        EstoqueFilial estoqueExistente = EstoqueFilial.builder()
                .id(1L)
                .filial(filial)
                .produto(produto)
                .quantidade(10)
                .build();

        EstoqueFilial estoqueAtualizado = EstoqueFilial.builder()
                .id(1L)
                .filial(filial)
                .produto(produto)
                .quantidade(5) // 10 - 5
                .build();

        EstoqueFilialDTO estoqueDTO = EstoqueFilialDTO.builder()
                .id(1L)
                .filialId(1L)
                .produtoId(1L)
                .quantidade(5)
                .build();

        when(estoqueFilialRepository.findByFilialIdAndProdutoId(1L, 1L)).thenReturn(Optional.of(estoqueExistente));
        when(estoqueFilialRepository.save(any(EstoqueFilial.class))).thenReturn(estoqueAtualizado);
        when(estoqueFilialMapper.toDTO(estoqueAtualizado)).thenReturn(estoqueDTO);

        // Act
        EstoqueFilialDTO resultado = estoqueFilialService.registrarSaida(saida);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(5, resultado.getQuantidade());
        verify(estoqueFilialRepository).findByFilialIdAndProdutoId(1L, 1L);
        verify(estoqueFilialRepository).save(any(EstoqueFilial.class));
        verify(estoqueFilialMapper).toDTO(estoqueAtualizado);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeInsuficienteParaSaida() {
        // Arrange
        MovimentacaoEstoqueDTO saida = new MovimentacaoEstoqueDTO();
        saida.setFilialId(1L);
        saida.setProdutoId(1L);
        saida.setQuantidade(15); // Mais do que o disponível

        Filial filial = new Filial();
        filial.setId(1L);
        Produto produto = new Produto();
        produto.setId(1L);

        EstoqueFilial estoqueExistente = EstoqueFilial.builder()
                .id(1L)
                .filial(filial)
                .produto(produto)
                .quantidade(10) // Menos do que o solicitado
                .build();

        when(estoqueFilialRepository.findByFilialIdAndProdutoId(1L, 1L)).thenReturn(Optional.of(estoqueExistente));

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            estoqueFilialService.registrarSaida(saida);
        });

        verify(estoqueFilialRepository).findByFilialIdAndProdutoId(1L, 1L);
        verify(estoqueFilialRepository, never()).save(any(EstoqueFilial.class));
        verify(estoqueFilialMapper, never()).toDTO(any(EstoqueFilial.class));
    }
}
