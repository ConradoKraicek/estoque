package com.estoque.service;

import com.estoque.exception.ResourceNotFoundException;
import com.estoque.model.EstoqueFilial;
import com.estoque.model.Filial;
import com.estoque.model.Produto;
import com.estoque.repository.EstoqueFilialRepository;
import com.estoque.repository.FilialRepository;
import com.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {
    private final EstoqueFilialRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final FilialRepository filialRepository;

    public EstoqueService(EstoqueFilialRepository estoqueRepository, ProdutoRepository produtoRepository, FilialRepository filialRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.filialRepository = filialRepository;
    }

    public void adicionarEstoque(Long filialId, Long produtoId, Integer quantidade) {
        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        EstoqueFilial estoque = estoqueRepository.findByFilialAndProduto(filial, produto)
                .orElse(new EstoqueFilial());

        if (estoque.getId() == null) {
            estoque.setFilial(filial);
            estoque.setProduto(produto);
            estoque.setQuantidade(quantidade);
        } else {
            estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        }

        estoqueRepository.save(estoque);
    }


    public void transferirEstoque(Long filialOrigemId, Long filialDestinoId, Long produtoId, Integer quantidade) {
        Filial filialOrigem = filialRepository.findById(filialOrigemId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial de origem não encontrada"));

        Filial filialDestino = filialRepository.findById(filialDestinoId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial de destino não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        EstoqueFilial estoqueOrigem = estoqueRepository.findByFilialAndProduto(filialOrigem, produto)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado na filial de origem"));

        if (estoqueOrigem.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente no estoque da filial de origem");
        }

        EstoqueFilial estoqueDestino = estoqueRepository.findByFilialAndProduto(filialDestino, produto)
                .orElse(new EstoqueFilial());

        if (estoqueDestino.getId() == null) {
            estoqueDestino.setFilial(filialDestino);
            estoqueDestino.setProduto(produto);
            estoqueDestino.setQuantidade(quantidade);
        } else {
            estoqueDestino.setQuantidade(estoqueDestino.getQuantidade() + quantidade);
        }

        estoqueOrigem.setQuantidade(estoqueOrigem.getQuantidade() - quantidade);

        estoqueRepository.save(estoqueOrigem);
        estoqueRepository.save(estoqueDestino);
    }

    public void baixarEstoque(Long filialId, Long produtoId, Integer quantidade) {
        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        EstoqueFilial estoque = estoqueRepository.findByFilialAndProduto(filial, produto)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));

        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente no estoque");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);

        estoqueRepository.save(estoque);
    }

}
