package com.estoque.service;


import com.estoque.dto.*;
import com.estoque.exception.*;
import com.estoque.mapper.EstoqueFilialMapper;
import com.estoque.model.*;
import com.estoque.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueFilialService {

    private final EstoqueFilialRepository estoqueFilialRepository;
    private final FilialRepository filialRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueFilialMapper estoqueFilialMapper;

    public List<EstoqueFilialDTO> listarTodos() {
        return estoqueFilialRepository.findAll().stream()
                .map(estoqueFilialMapper::toDTO)
                .toList();
    }

    public List<EstoqueFilialDTO> buscarPorFilial(Long filialId) {
        return estoqueFilialRepository.findByFilialId(filialId).stream()
                .map(estoqueFilialMapper::toDTO)
                .toList();
    }

    public List<EstoqueFilialDTO> buscarPorProduto(Long produtoId) {
        return estoqueFilialRepository.findByProdutoId(produtoId).stream()
                .map(estoqueFilialMapper::toDTO)
                .toList();
    }

    @Transactional
    public EstoqueFilialDTO atualizarEstoque(Long id, Integer quantidade) {
        EstoqueFilial estoque = estoqueFilialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de estoque não encontrado"));

        estoque.setQuantidade(quantidade);
        return estoqueFilialMapper.toDTO(estoqueFilialRepository.save(estoque));
    }

    @Transactional
    public void transferirEstoque(TransferenciaEstoqueDTO transferencia) {
        if (transferencia.getOrigemId().equals(transferencia.getDestinoId())) {
            throw new BusinessException("Filial de origem e destino devem ser diferentes");
        }

        EstoqueFilial origem = getEstoque(transferencia.getOrigemId(), transferencia.getProdutoId());
        EstoqueFilial destino = getOrCreateEstoque(transferencia.getDestinoId(), transferencia.getProdutoId());

        if (origem.getQuantidade() < transferencia.getQuantidade()) {
            throw new BusinessException("Quantidade insuficiente em estoque");
        }

        origem.setQuantidade(origem.getQuantidade() - transferencia.getQuantidade());
        destino.setQuantidade(destino.getQuantidade() + transferencia.getQuantidade());

        estoqueFilialRepository.save(origem);
        estoqueFilialRepository.save(destino);
    }

    @Transactional
    public EstoqueFilialDTO registrarEntrada(MovimentacaoEstoqueDTO entrada) {
        EstoqueFilial estoque = getOrCreateEstoque(entrada.getFilialId(), entrada.getProdutoId());
        estoque.setQuantidade(estoque.getQuantidade() + entrada.getQuantidade());
        return estoqueFilialMapper.toDTO(estoqueFilialRepository.save(estoque));
    }

    @Transactional
    public EstoqueFilialDTO registrarSaida(MovimentacaoEstoqueDTO saida) {
        EstoqueFilial estoque = getEstoque(saida.getFilialId(), saida.getProdutoId());

        if (estoque.getQuantidade() < saida.getQuantidade()) {
            throw new BusinessException("Quantidade insuficiente em estoque");
        }

        estoque.setQuantidade(estoque.getQuantidade() - saida.getQuantidade());
        return estoqueFilialMapper.toDTO(estoqueFilialRepository.save(estoque));
    }

    private EstoqueFilial getEstoque(Long filialId, Long produtoId) {
        return estoqueFilialRepository.findByFilialIdAndProdutoId(filialId, produtoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Estoque não encontrado para filial %d e produto %d", filialId, produtoId)));
    }

    private EstoqueFilial getOrCreateEstoque(Long filialId, Long produtoId) {
        return estoqueFilialRepository.findByFilialIdAndProdutoId(filialId, produtoId)
                .orElseGet(() -> {
                    Filial filial = filialRepository.findById(filialId)
                            .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada"));

                    Produto produto = produtoRepository.findById(produtoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                    return estoqueFilialRepository.save(
                            EstoqueFilial.builder()
                                    .filial(filial)
                                    .produto(produto)
                                    .quantidade(0)
                                    .build());
                });
    }
}
