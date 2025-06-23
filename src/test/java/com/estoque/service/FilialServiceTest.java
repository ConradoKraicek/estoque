package com.estoque.service;

import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.exception.DuplicateResourceException;
import com.estoque.exception.ResourceNotFoundException;
import com.estoque.mapper.FilialMapper;
import com.estoque.model.Filial;
import com.estoque.repository.FilialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilialServiceTest {

    @Mock
    private FilialRepository filialRepository;

    @Mock
    private FilialMapper filialMapper;

    @InjectMocks
    private FilialService filialService;

    @Test
    void deveListarTodasAsFiliais() {
        // Arrange
        Filial filial1 = new Filial();
        filial1.setId(1L);
        filial1.setNome("Filial Centro");
        filial1.setEndereco("Rua A, 123");
        filial1.setTelefone("11987654321");

        Filial filial2 = new Filial();
        filial2.setId(2L);
        filial2.setNome("Filial Norte");
        filial2.setEndereco("Rua B, 456");
        filial2.setTelefone("11987654322");

        List<Filial> filiais = Arrays.asList(filial1, filial2);

        List<FilialDTO> filiaisDTO = Arrays.asList(
                FilialDTO.builder().id(1L).nome("Filial Centro").endereco("Rua A, 123").telefone("11987654321").build(),
                FilialDTO.builder().id(2L).nome("Filial Norte").endereco("Rua B, 456").telefone("11987654322").build()
        );

        when(filialRepository.findAll()).thenReturn(filiais);
        when(filialMapper.toDTO(filiais.get(0))).thenReturn(filiaisDTO.get(0));
        when(filialMapper.toDTO(filiais.get(1))).thenReturn(filiaisDTO.get(1));

        // Act
        List<FilialDTO> resultado = filialService.listarTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Filial Centro", resultado.get(0).getNome());
        assertEquals("Filial Norte", resultado.get(1).getNome());
        verify(filialRepository).findAll();
        verify(filialMapper, times(2)).toDTO(any(Filial.class));
    }

    @Test
    void deveBuscarFilialPorIdComSucesso() {
        // Arrange
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial Centro");
        filial.setEndereco("Rua A, 123");
        filial.setTelefone("11987654321");

        FilialDTO filialDTO = FilialDTO.builder()
                .id(1L)
                .nome("Filial Centro")
                .endereco("Rua A, 123")
                .telefone("11987654321")
                .build();

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));
        when(filialMapper.toDTO(filial)).thenReturn(filialDTO);

        // Act
        FilialDTO resultado = filialService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Filial Centro", resultado.getNome());
        verify(filialRepository).findById(1L);
        verify(filialMapper).toDTO(filial);
    }

    @Test
    void deveLancarExcecaoQuandoFilialNaoEncontrada() {
        // Arrange
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            filialService.buscarPorId(1L);
        });

        verify(filialRepository).findById(1L);
        verify(filialMapper, never()).toDTO(any(Filial.class));
    }

    @Test
    void deveCriarFilialComSucesso() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Nova Filial");
        request.setEndereco("Rua Nova, 789");
        request.setTelefone("11987654323");

        Filial filial = new Filial();
        filial.setNome("Nova Filial");
        filial.setEndereco("Rua Nova, 789");
        filial.setTelefone("11987654323");

        Filial filialSalva = new Filial();
        filialSalva.setId(3L);
        filialSalva.setNome("Nova Filial");
        filialSalva.setEndereco("Rua Nova, 789");
        filialSalva.setTelefone("11987654323");

        FilialDTO filialDTO = FilialDTO.builder()
                .id(3L)
                .nome("Nova Filial")
                .endereco("Rua Nova, 789")
                .telefone("11987654323")
                .build();

        when(filialRepository.existsByNome("Nova Filial")).thenReturn(false);
        when(filialMapper.requestToEntity(request)).thenReturn(filial);
        when(filialRepository.save(filial)).thenReturn(filialSalva);
        when(filialMapper.toDTO(filialSalva)).thenReturn(filialDTO);

        // Act
        FilialDTO resultado = filialService.criar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Nova Filial", resultado.getNome());
        verify(filialRepository).existsByNome("Nova Filial");
        verify(filialMapper).requestToEntity(request);
        verify(filialRepository).save(filial);
        verify(filialMapper).toDTO(filialSalva);
    }

    @Test
    void deveLancarExcecaoQuandoCriarFilialComNomeDuplicado() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Existente");

        when(filialRepository.existsByNome("Filial Existente")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            filialService.criar(request);
        });

        verify(filialRepository).existsByNome("Filial Existente");
        verify(filialMapper, never()).requestToEntity(any());
        verify(filialRepository, never()).save(any());
    }

    @Test
    void deveAtualizarFilialComSucesso() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Atualizada");
        request.setEndereco("Rua Atualizada, 123");
        request.setTelefone("11987654324");

        Filial filialExistente = new Filial();
        filialExistente.setId(1L);
        filialExistente.setNome("Filial Original");
        filialExistente.setEndereco("Rua Original, 123");
        filialExistente.setTelefone("11987654321");

        Filial filialAtualizada = new Filial();
        filialAtualizada.setId(1L);
        filialAtualizada.setNome("Filial Atualizada");
        filialAtualizada.setEndereco("Rua Atualizada, 123");
        filialAtualizada.setTelefone("11987654324");

        FilialDTO filialDTO = FilialDTO.builder()
                .id(1L)
                .nome("Filial Atualizada")
                .endereco("Rua Atualizada, 123")
                .telefone("11987654324")
                .build();

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filialExistente));
        when(filialRepository.existsByNome("Filial Atualizada")).thenReturn(false);
        when(filialRepository.save(any(Filial.class))).thenReturn(filialAtualizada);
        when(filialMapper.toDTO(filialAtualizada)).thenReturn(filialDTO);

        // Act
        FilialDTO resultado = filialService.atualizar(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Filial Atualizada", resultado.getNome());
        assertEquals("Rua Atualizada, 123", resultado.getEndereco());
        assertEquals("11987654324", resultado.getTelefone());
        verify(filialRepository).findById(1L);
        verify(filialRepository).existsByNome("Filial Atualizada");
        verify(filialRepository).save(any(Filial.class));
        verify(filialMapper).toDTO(filialAtualizada);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarFilialComNomeDuplicado() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Existente");

        Filial filialExistente = new Filial();
        filialExistente.setId(1L);
        filialExistente.setNome("Filial Original");

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filialExistente));
        when(filialRepository.existsByNome("Filial Existente")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            filialService.atualizar(1L, request);
        });

        verify(filialRepository).findById(1L);
        verify(filialRepository).existsByNome("Filial Existente");
        verify(filialRepository, never()).save(any(Filial.class));
    }

    @Test
    void deveExcluirFilial() {
        // Arrange
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial");
        filial.setEndereco("Endereço");
        filial.setTelefone("11987654321");

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));

        // Act
        filialService.excluir(1L);

        // Assert
        verify(filialRepository).findById(1L);

        verify(filialRepository).save(filial);
        verify(filialRepository, never()).delete(any(Filial.class));
    }
}
