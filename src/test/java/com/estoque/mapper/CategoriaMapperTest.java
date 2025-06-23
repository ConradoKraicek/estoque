package com.estoque.mapper;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.CategoriaRequest;
import com.estoque.model.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest
public class CategoriaMapperTest {

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Test
    void deveMaperarEntityParaDTO() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        categoria.setDescricao("Descrição da categoria teste");
        categoria.setProdutos(new ArrayList<>());

        // Act
        CategoriaDTO dto = categoriaMapper.toDTO(categoria);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Categoria Teste", dto.getNome());
        assertEquals("Descrição da categoria teste", dto.getDescricao());
    }

    @Test
    void deveMaperarDTOParaEntity() {
        // Arrange
        CategoriaDTO dto = CategoriaDTO.builder()
                .id(1L)
                .nome("Categoria Teste")
                .descricao("Descrição da categoria teste")
                .build();

        // Act
        Categoria entity = categoriaMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Categoria Teste", entity.getNome());
        assertEquals("Descrição da categoria teste", entity.getDescricao());
    }

    @Test
    void deveMaperarRequestParaEntity() {
        // Arrange
        CategoriaRequest request = new CategoriaRequest();
        request.setNome("Categoria Teste");
        request.setDescricao("Descrição da categoria teste");

        // Act
        Categoria entity = categoriaMapper.requestToEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(null, entity.getId()); // ID deve ser ignorado conforme @Mapping(target = "id", ignore = true)
        assertEquals("Categoria Teste", entity.getNome());
        assertEquals("Descrição da categoria teste", entity.getDescricao());
        // O MapStruct pode inicializar a lista como vazia em vez de null, o que é aceitável
        assertNotSame(null, entity.getProdutos()); // Produtos deve ser ignorado, mas pode ser inicializado como lista vazia
    }
}
