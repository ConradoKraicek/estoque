package com.estoque.service;


import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.CategoriaRequest;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.mapper.CategoriaMapper;
import com.estoque.model.Categoria;
import com.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

//    public Page<CategoriaDTO> listarTodos(Pageable pageable) {
//        return (Page<CategoriaDTO>) categoriaRepository.findAll().stream()
//                .map(categoriaMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    public Page<CategoriaDTO> listarTodos(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(categoriaMapper::toDTO);
    }


    public CategoriaDTO buscarPorId(Long id) {
        return categoriaMapper.toDTO(getCategoria(id));
    }

    public CategoriaDTO salvar(CategoriaRequest request) {
        if (categoriaRepository.existsByNome(request.getNome())) {
            throw new DuplicateResourceException("Já existe uma categoria com este nome");
        }
        Categoria categoria = categoriaMapper.requestToEntity(request);
        return categoriaMapper.toDTO(categoriaRepository.save(categoria));
    }

    public CategoriaDTO atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = getCategoria(id);

        if (!categoria.getNome().equals(request.getNome()) &&
                categoriaRepository.existsByNome(request.getNome())) {
            throw new DuplicateResourceException("Já existe uma categoria com este nome");
        }

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());

        return categoriaMapper.toDTO(categoriaRepository.save(categoria));
    }

    public void excluir(Long id) {
        Categoria categoria = getCategoria(id);
        if (!categoria.getProdutos().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir categoria com produtos vinculados");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria getCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }
}
