package com.estoque.service;


import com.estoque.exception.ResourceNotFoundException;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.model.Categoria;
import com.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public Categoria salvar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new DuplicateResourceException("Já existe uma categoria com este nome");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    if (!categoria.getNome().equals(categoriaAtualizada.getNome()) &&
                            categoriaRepository.existsByNome(categoriaAtualizada.getNome())) {
                        throw new DuplicateResourceException("Já existe uma categoria com este nome");
                    }
                    categoria.setNome(categoriaAtualizada.getNome());
                    categoria.setDescricao(categoriaAtualizada.getDescricao());
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);
        if (!categoria.getProdutos().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir categoria com produtos vinculados");
        }
        categoriaRepository.delete(categoria);
    }
}