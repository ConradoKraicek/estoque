package com.estoque.mapper;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ProdutoMapperTest {

    @Autowired
    private ProdutoMapper produtoMapper;

    @Test
    void deveMaperarEntityParaDTO() {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPreco(100.0);
        produto.setEstoqueMinimo(10);

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria Teste");
        produto.setCategoria(categoria);

        // Act
        ProdutoDTO dto = produtoMapper.toDTO(produto);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Produto Teste", dto.getNome());
        assertEquals(100.0, dto.getPreco());
        assertEquals(10, dto.getEstoqueMinimo());
        assertNotNull(dto.getCategoria());
        assertEquals(1L, dto.getCategoria().getId());
        assertEquals("Categoria Teste", dto.getCategoria().getNome());
    }

    @Test
    void deveMaperarDTOParaEntity() {
        // Arrange
        ProdutoDTO dto = ProdutoDTO.builder()
                .id(1L)
                .nome("Produto Teste")
                .preco(100.0)
                .estoqueMinimo(10)
                .categoria(CategoriaDTO.builder()
                        .id(1L)
                        .nome("Categoria Teste")
                        .build())
                .build();

        // Act
        Produto entity = produtoMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Produto Teste", entity.getNome());
        assertEquals(100.0, entity.getPreco());
        assertEquals(10, entity.getEstoqueMinimo());
        assertNotNull(entity.getCategoria());
        assertEquals(1L, entity.getCategoria().getId());
        assertEquals("Categoria Teste", entity.getCategoria().getNome());
    }

    @Test
    void deveMaperarRequestParaEntity() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Teste");
        request.setPreco(100.0);
        request.setEstoqueMinimo(10);
        request.setCategoriaId(1L);

        // Act
        Produto entity = produtoMapper.requestToEntity(request);

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId()); // ID deve ser ignorado conforme @Mapping(target = "id", ignore = true)
        assertEquals("Produto Teste", entity.getNome());
        assertEquals(100.0, entity.getPreco());
        assertEquals(10, entity.getEstoqueMinimo());
        assertNull(entity.getCategoria()); // Categoria deve ser ignorada conforme @Mapping(target = "categoria", ignore = true)
    }
}
