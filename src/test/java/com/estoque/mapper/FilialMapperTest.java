package com.estoque.mapper;

import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.model.Filial;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class FilialMapperTest {

    @Autowired
    private FilialMapper filialMapper;

    @Test
    void deveMaperarEntityParaDTO() {
        // Arrange
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial Centro");
        filial.setEndereco("Rua das Flores, 123");
        filial.setTelefone("11987654321");

        // Act
        FilialDTO dto = filialMapper.toDTO(filial);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Filial Centro", dto.getNome());
        assertEquals("Rua das Flores, 123", dto.getEndereco());
        assertEquals("11987654321", dto.getTelefone());
    }

    @Test
    void deveMaperarDTOParaEntity() {
        // Arrange
        FilialDTO dto = FilialDTO.builder()
                .id(1L)
                .nome("Filial Centro")
                .endereco("Rua das Flores, 123")
                .telefone("11987654321")
                .build();

        // Act
        Filial entity = filialMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Filial Centro", entity.getNome());
        assertEquals("Rua das Flores, 123", entity.getEndereco());
        assertEquals("11987654321", entity.getTelefone());
    }

    @Test
    void deveMaperarRequestParaEntity() {
        // Arrange
        FilialRequest request = new FilialRequest();
        request.setNome("Filial Centro");
        request.setEndereco("Rua das Flores, 123");
        request.setTelefone("11987654321");

        // Act
        Filial entity = filialMapper.requestToEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId()); // ID deve ser ignorado conforme @Mapping(target = "id", ignore = true)
        assertEquals("Filial Centro", entity.getNome());
        assertEquals("Rua das Flores, 123", entity.getEndereco());
        assertEquals("11987654321", entity.getTelefone());
    }
}
