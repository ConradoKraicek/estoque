package com.estoque.service;

import com.estoque.exception.ResourceNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstoqueServiceTest {

    @Mock
    private EstoqueFilialRepository estoqueRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private FilialRepository filialRepository;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void deveAdicionarEstoqueQuandoEstoqueNaoExiste() {
        // Arrange
        Long filialId = 1L;
        Long produtoId = 1L;
        Integer quantidade = 10;

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome("Filial Centro");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        when(filialRepository.findById(filialId)).thenReturn(Optional.of(filial));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filial, produto)).thenReturn(Optional.empty());

        // Act
        estoqueService.adicionarEstoque(filialId, produtoId, quantidade);

        // Assert
        verify(filialRepository).findById(filialId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filial, produto);
        verify(estoqueRepository).save(any(EstoqueFilial.class));
    }

    @Test
    void deveAdicionarEstoqueQuandoEstoqueJaExiste() {
        // Arrange
        Long filialId = 1L;
        Long produtoId = 1L;
        Integer quantidade = 10;

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome("Filial Centro");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoqueExistente = new EstoqueFilial();
        estoqueExistente.setId(1L);
        estoqueExistente.setFilial(filial);
        estoqueExistente.setProduto(produto);
        estoqueExistente.setQuantidade(5);

        when(filialRepository.findById(filialId)).thenReturn(Optional.of(filial));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filial, produto)).thenReturn(Optional.of(estoqueExistente));

        // Act
        estoqueService.adicionarEstoque(filialId, produtoId, quantidade);

        // Assert
        verify(filialRepository).findById(filialId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filial, produto);
        verify(estoqueRepository).save(estoqueExistente);
    }

    @Test
    void deveLancarExcecaoQuandoFilialNaoEncontradaAoAdicionarEstoque() {
        // Arrange
        Long filialId = 999L;
        Long produtoId = 1L;
        Integer quantidade = 10;

        when(filialRepository.findById(filialId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            estoqueService.adicionarEstoque(filialId, produtoId, quantidade);
        });

        verify(filialRepository).findById(filialId);
        verify(produtoRepository, never()).findById(any());
        verify(estoqueRepository, never()).findByFilialAndProduto(any(), any());
        verify(estoqueRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoAoAdicionarEstoque() {
        // Arrange
        Long filialId = 1L;
        Long produtoId = 999L;
        Integer quantidade = 10;

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome("Filial Centro");

        when(filialRepository.findById(filialId)).thenReturn(Optional.of(filial));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            estoqueService.adicionarEstoque(filialId, produtoId, quantidade);
        });

        verify(filialRepository).findById(filialId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository, never()).findByFilialAndProduto(any(), any());
        verify(estoqueRepository, never()).save(any());
    }

    @Test
    void deveTransferirEstoqueComSucesso() {
        // Arrange
        Long filialOrigemId = 1L;
        Long filialDestinoId = 2L;
        Long produtoId = 1L;
        Integer quantidade = 5;

        Filial filialOrigem = new Filial();
        filialOrigem.setId(filialOrigemId);
        filialOrigem.setNome("Filial Origem");

        Filial filialDestino = new Filial();
        filialDestino.setId(filialDestinoId);
        filialDestino.setNome("Filial Destino");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoqueOrigem = new EstoqueFilial();
        estoqueOrigem.setId(1L);
        estoqueOrigem.setFilial(filialOrigem);
        estoqueOrigem.setProduto(produto);
        estoqueOrigem.setQuantidade(10);

        EstoqueFilial estoqueDestino = new EstoqueFilial();
        estoqueDestino.setId(2L);
        estoqueDestino.setFilial(filialDestino);
        estoqueDestino.setProduto(produto);
        estoqueDestino.setQuantidade(2);

        when(filialRepository.findById(filialOrigemId)).thenReturn(Optional.of(filialOrigem));
        when(filialRepository.findById(filialDestinoId)).thenReturn(Optional.of(filialDestino));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filialOrigem, produto)).thenReturn(Optional.of(estoqueOrigem));
        when(estoqueRepository.findByFilialAndProduto(filialDestino, produto)).thenReturn(Optional.of(estoqueDestino));

        // Act
        estoqueService.transferirEstoque(filialOrigemId, filialDestinoId, produtoId, quantidade);

        // Assert
        verify(filialRepository).findById(filialOrigemId);
        verify(filialRepository).findById(filialDestinoId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filialOrigem, produto);
        verify(estoqueRepository).findByFilialAndProduto(filialDestino, produto);
        // Vamos verificar explicitamente as duas chamadas esperadas do método save
        verify(estoqueRepository, times(1)).save(estoqueOrigem);
        verify(estoqueRepository, times(1)).save(estoqueDestino);
    }

    @Test
    void deveTransferirEstoqueQuandoDestinoNaoExiste() {
        // Arrange
        Long filialOrigemId = 1L;
        Long filialDestinoId = 2L;
        Long produtoId = 1L;
        Integer quantidade = 5;

        Filial filialOrigem = new Filial();
        filialOrigem.setId(filialOrigemId);
        filialOrigem.setNome("Filial Origem");

        Filial filialDestino = new Filial();
        filialDestino.setId(filialDestinoId);
        filialDestino.setNome("Filial Destino");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoqueOrigem = new EstoqueFilial();
        estoqueOrigem.setId(1L);
        estoqueOrigem.setFilial(filialOrigem);
        estoqueOrigem.setProduto(produto);
        estoqueOrigem.setQuantidade(10);

        when(filialRepository.findById(filialOrigemId)).thenReturn(Optional.of(filialOrigem));
        when(filialRepository.findById(filialDestinoId)).thenReturn(Optional.of(filialDestino));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filialOrigem, produto)).thenReturn(Optional.of(estoqueOrigem));
        when(estoqueRepository.findByFilialAndProduto(filialDestino, produto)).thenReturn(Optional.empty());

        // Act
        estoqueService.transferirEstoque(filialOrigemId, filialDestinoId, produtoId, quantidade);

        // Assert
        verify(filialRepository).findById(filialOrigemId);
        verify(filialRepository).findById(filialDestinoId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filialOrigem, produto);
        verify(estoqueRepository).findByFilialAndProduto(filialDestino, produto);
        // Vamos verificar explicitamente as duas chamadas esperadas do método save
        verify(estoqueRepository, times(2)).save(any(EstoqueFilial.class));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeInsuficienteParaTransferencia() {
        // Arrange
        Long filialOrigemId = 1L;
        Long filialDestinoId = 2L;
        Long produtoId = 1L;
        Integer quantidade = 15; // Mais do que o disponível

        Filial filialOrigem = new Filial();
        filialOrigem.setId(filialOrigemId);
        filialOrigem.setNome("Filial Origem");

        Filial filialDestino = new Filial();
        filialDestino.setId(filialDestinoId);
        filialDestino.setNome("Filial Destino");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoqueOrigem = new EstoqueFilial();
        estoqueOrigem.setId(1L);
        estoqueOrigem.setFilial(filialOrigem);
        estoqueOrigem.setProduto(produto);
        estoqueOrigem.setQuantidade(10); // Menos do que o solicitado

        when(filialRepository.findById(filialOrigemId)).thenReturn(Optional.of(filialOrigem));
        when(filialRepository.findById(filialDestinoId)).thenReturn(Optional.of(filialDestino));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filialOrigem, produto)).thenReturn(Optional.of(estoqueOrigem));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            estoqueService.transferirEstoque(filialOrigemId, filialDestinoId, produtoId, quantidade);
        });

        verify(filialRepository).findById(filialOrigemId);
        verify(filialRepository).findById(filialDestinoId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filialOrigem, produto);
        verify(estoqueRepository, never()).save(any());
    }

    @Test
    void deveBaixarEstoqueComSucesso() {
        // Arrange
        Long filialId = 1L;
        Long produtoId = 1L;
        Integer quantidade = 5;

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome("Filial Centro");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoque = new EstoqueFilial();
        estoque.setId(1L);
        estoque.setFilial(filial);
        estoque.setProduto(produto);
        estoque.setQuantidade(10);

        when(filialRepository.findById(filialId)).thenReturn(Optional.of(filial));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filial, produto)).thenReturn(Optional.of(estoque));

        // Act
        estoqueService.baixarEstoque(filialId, produtoId, quantidade);

        // Assert
        verify(filialRepository).findById(filialId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filial, produto);
        verify(estoqueRepository).save(estoque);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeInsuficienteParaBaixa() {
        // Arrange
        Long filialId = 1L;
        Long produtoId = 1L;
        Integer quantidade = 15; // Mais do que o disponível

        Filial filial = new Filial();
        filial.setId(filialId);
        filial.setNome("Filial Centro");

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Smartphone");

        EstoqueFilial estoque = new EstoqueFilial();
        estoque.setId(1L);
        estoque.setFilial(filial);
        estoque.setProduto(produto);
        estoque.setQuantidade(10); // Menos do que o solicitado

        when(filialRepository.findById(filialId)).thenReturn(Optional.of(filial));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(estoqueRepository.findByFilialAndProduto(filial, produto)).thenReturn(Optional.of(estoque));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            estoqueService.baixarEstoque(filialId, produtoId, quantidade);
        });

        verify(filialRepository).findById(filialId);
        verify(produtoRepository).findById(produtoId);
        verify(estoqueRepository).findByFilialAndProduto(filial, produto);
        verify(estoqueRepository, never()).save(any());
    }
}
