package com.estoque.service;


import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.FilialMapper;
import com.estoque.model.Filial;
import com.estoque.repository.FilialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilialService {

    private final FilialRepository filialRepository;
    private final FilialMapper filialMapper;

    public List<FilialDTO> listarTodas() {
        return filialRepository.findAll().stream()
                .map(filialMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FilialDTO buscarPorId(Long id) {
        return filialMapper.toDTO(getFilial(id));
    }

    @Transactional
    public FilialDTO criar(FilialRequest request) {
        if (filialRepository.existsByNome(request.getNome())) {
            throw new DuplicateResourceException("Já existe uma filial com este nome");
        }

        Filial filial = filialMapper.requestToEntity(request);
        return filialMapper.toDTO(filialRepository.save(filial));
    }

    @Transactional
    public FilialDTO atualizar(Long id, FilialRequest request) {
        Filial filial = getFilial(id);

        if (!filial.getNome().equals(request.getNome()) &&
                filialRepository.existsByNome(request.getNome())) {
            throw new DuplicateResourceException("Já existe uma filial com este nome");
        }

        filial.setNome(request.getNome());
        filial.setEndereco(request.getEndereco());
        filial.setTelefone(request.getTelefone());

        return filialMapper.toDTO(filialRepository.save(filial));
    }

    @Transactional
    public void excluir(Long id) {
        Filial filial = getFilial(id);
        filialRepository.save(filial);
    }

    private Filial getFilial(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada"));
    }
}
