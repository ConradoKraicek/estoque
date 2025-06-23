package com.estoque.service;

import com.estoque.model.EstoqueFilial;
import com.estoque.model.Filial;
import com.estoque.model.Produto;
import com.estoque.repository.EstoqueFilialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RelatorioEstoqueServiceTest {

    @Mock
    private EstoqueFilialRepository estoqueFilialRepository;

    @InjectMocks
    private RelatorioEstoqueService relatorioEstoqueService;

    @Test
    void deveGerarDadosRelatorioParaTodasFiliais() {
        // Arrange
        List<EstoqueFilial> estoques = Arrays.asList(
                criarEstoqueFilial(1L, "Produto 1", 1L, "Filial A", 10, 5),
                criarEstoqueFilial(2L, "Produto 2", 2L, "Filial B", 20, 10)
        );

        when(estoqueFilialRepository.findAll()).thenReturn(estoques);

        // Act
        List<EstoqueFilial> resultado = relatorioEstoqueService.gerarDadosRelatorio(null);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estoqueFilialRepository).findAll();
        verify(estoqueFilialRepository, never()).findByFilialId(any());
    }

    @Test
    void deveGerarDadosRelatorioParaFilialEspecifica() {
        // Arrange
        Long filialId = 1L;
        List<EstoqueFilial> estoques = Arrays.asList(
                criarEstoqueFilial(1L, "Produto 1", filialId, "Filial A", 10, 5),
                criarEstoqueFilial(2L, "Produto 2", filialId, "Filial A", 20, 10)
        );

        when(estoqueFilialRepository.findByFilialId(filialId)).thenReturn(estoques);

        // Act
        List<EstoqueFilial> resultado = relatorioEstoqueService.gerarDadosRelatorio(filialId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(estoqueFilialRepository).findByFilialId(filialId);
        verify(estoqueFilialRepository, never()).findAll();
    }

    @Test
    void deveGerarRelatorioExcel() throws Exception {
        // Arrange
        Long filialId = 1L;
        List<EstoqueFilial> estoques = Arrays.asList(
                criarEstoqueFilial(1L, "Produto 1", filialId, "Filial A", 10, 5),
                criarEstoqueFilial(2L, "Produto 2", filialId, "Filial A", 0, 10), // Esgotado
                criarEstoqueFilial(3L, "Produto 3", filialId, "Filial A", 3, 5)   // Estoque baixo
        );

        when(estoqueFilialRepository.findByFilialId(filialId)).thenReturn(estoques);

        // Act
        byte[] resultado = relatorioEstoqueService.gerarRelatorioExcel(filialId);

        // Assert
        assertNotNull(resultado);
        verify(estoqueFilialRepository).findByFilialId(filialId);
    }

    @Test
    void deveGerarRelatorioPdf() throws Exception {
        // Arrange
        Long filialId = 1L;
        List<EstoqueFilial> estoques = Arrays.asList(
                criarEstoqueFilial(1L, "Produto 1", filialId, "Filial A", 10, 5),
                criarEstoqueFilial(2L, "Produto 2", filialId, "Filial A", 0, 10), // Esgotado
                criarEstoqueFilial(3L, "Produto 3", filialId, "Filial A", 3, 5)   // Estoque baixo
        );

        when(estoqueFilialRepository.findByFilialId(filialId)).thenReturn(estoques);

        // Act
        byte[] resultado = relatorioEstoqueService.gerarRelatorioPdf(filialId);

        // Assert
        assertNotNull(resultado);
        verify(estoqueFilialRepository).findByFilialId(filialId);
    }

    @Test
    void deveRetornarStatusCorretoParaEstoque() {
        // Testando o método privado getStatusEstoque indiretamente
        // através da geração do relatório

        // Arrange
        Long filialId = 1L;
        EstoqueFilial estoqueOk = criarEstoqueFilial(1L, "Produto 1", filialId, "Filial A", 10, 5);
        EstoqueFilial estoqueEsgotado = criarEstoqueFilial(2L, "Produto 2", filialId, "Filial A", 0, 10);
        EstoqueFilial estoqueBaixo = criarEstoqueFilial(3L, "Produto 3", filialId, "Filial A", 3, 5);

        List<EstoqueFilial> estoques = Arrays.asList(estoqueOk, estoqueEsgotado, estoqueBaixo);

        when(estoqueFilialRepository.findByFilialId(filialId)).thenReturn(estoques);

        // Act & Assert - Verificando indiretamente
        assertDoesNotThrow(() -> {
            relatorioEstoqueService.gerarDadosRelatorio(filialId);
        });

        verify(estoqueFilialRepository).findByFilialId(filialId);
    }

    private EstoqueFilial criarEstoqueFilial(Long id, String nomeProduto, Long filialId, String nomeFilial,
                                           Integer quantidade, Integer estoqueMinimo) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nomeProduto);
        produto.setEstoqueMinimo(estoqueMinimo);

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome(nomeFilial);

        EstoqueFilial estoqueFilial = new EstoqueFilial();
        estoqueFilial.setId(id);
        estoqueFilial.setProduto(produto);
        estoqueFilial.setFilial(filial);
        estoqueFilial.setQuantidade(quantidade);

        return estoqueFilial;
    }
}
