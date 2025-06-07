package com.estoque.service;

import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.ProdutoMapper;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(Long id) {
        return produtoMapper.toDTO(getProduto(id));
    }

    public ProdutoDTO salvar(ProdutoRequest request) {
        Produto produto = produtoMapper.requestToEntity(request);

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
            produto.setCategoria(categoria);
        }

        if (produto.getCodigoBarras() != null && produtoRepository.existsByCodigoBarras(produto.getCodigoBarras())) {
            throw new DuplicateResourceException("Já existe um produto com este código de barras");
        }

        return produtoMapper.toDTO(produtoRepository.save(produto));
    }

    public ProdutoDTO atualizar(Long id, ProdutoRequest request) {
        Produto produtoExistente = getProduto(id);

        produtoExistente.setNome(request.getNome());
        produtoExistente.setDescricao(request.getDescricao());
        produtoExistente.setPreco(request.getPreco());
        produtoExistente.setEstoqueMinimo(request.getEstoqueMinimo());

        // Verificar se o código de barras foi alterado e se já existe
        if (request.getCodigoBarras() != null && 
            !request.getCodigoBarras().equals(produtoExistente.getCodigoBarras()) && 
            produtoRepository.existsByCodigoBarras(request.getCodigoBarras())) {
            throw new DuplicateResourceException("Já existe um produto com este código de barras");
        }

        produtoExistente.setCodigoBarras(request.getCodigoBarras());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", request.getCategoriaId()));
            produtoExistente.setCategoria(categoria);
        }

        return produtoMapper.toDTO(produtoRepository.save(produtoExistente));
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<ProdutoDTO> findByCategoria(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", categoriaId));
        return produtoRepository.findByCategoria(categoria).stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Produto getProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
    }
}
