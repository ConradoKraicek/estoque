package com.estoque.service;

import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
    }

    public Produto salvar(Produto produto) {
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", produto.getCategoria().getId()));
            produto.setCategoria(categoria);
        }

        if (produto.getCodigoBarras() != null && produtoRepository.existsByCodigoBarras(produto.getCodigoBarras())) {
            throw new DuplicateResourceException("Já existe um produto com este código de barras");
        }

        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produto) {
       return produtoRepository.findById(id).map(produtoExistente -> {
            produtoExistente.setNome(produto.getNome());
            produtoExistente.setDescricao(produto.getDescricao());
            produtoExistente.setPreco(produto.getPreco());
            produtoExistente.setEstoqueMinimo(produto.getEstoqueMinimo());
            produtoExistente.setCodigoBarras(produto.getCodigoBarras());

            if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
                Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", produto.getCategoria().getId()));
                produtoExistente.setCategoria(categoria);
            }

            return produtoRepository.save(produtoExistente);
        }).orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<Produto> findByCategoria(Categoria categoria) {
        return produtoRepository.findByCategoria(categoria);
    }
}
