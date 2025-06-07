package com.estoque.controller;

import com.estoque.dto.CategoriaDTO;
import com.estoque.dto.ProdutoDTO;
import com.estoque.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarListaDeProdutos() throws Exception {
        // Arrange
        List<ProdutoDTO> produtos = Arrays.asList(
                ProdutoDTO.builder()
                    .id(1L)
                    .nome("Notebook")
                    .descricao("Dell i7")
                    .preco(4500.00)
                    .build(),
                ProdutoDTO.builder()
                    .id(2L)
                    .nome("Mouse")
                    .descricao("Sem fio")
                    .preco(120.00)
                    .build()
        );

        when(produtoService.listarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Notebook")));
    }
}
