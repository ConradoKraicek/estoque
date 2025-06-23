package com.estoque.controller;

import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.service.CategoriaService;
import com.estoque.service.ProdutoService;
import com.estoque.service.RelatorioProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private RelatorioProdutoService relatorioProdutoService;

    @InjectMocks
    private ProdutoController produtoController;

    @Test
    void deveListarTodosOsProdutos() {
        // Arrange
        List<ProdutoDTO> produtos = Arrays.asList(
                ProdutoDTO.builder().id(1L).nome("Produto 1").preco(10.0).build(),
                ProdutoDTO.builder().id(2L).nome("Produto 2").preco(20.0).build()
        );

        when(produtoService.listarTodos()).thenReturn(produtos);

        // Act
        ResponseEntity<List<ProdutoDTO>> response = produtoController.listarTodos();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(produtoService).listarTodos();
    }

    @Test
    void deveBuscarProdutoPorId() {
        // Arrange
        Long id = 1L;
        ProdutoDTO produto = ProdutoDTO.builder()
                .id(id)
                .nome("Produto Teste")
                .descricao("Descrição do produto")
                .preco(15.0)
                .build();

        when(produtoService.buscarPorId(id)).thenReturn(produto);

        // Act
        ResponseEntity<ProdutoDTO> response = produtoController.buscarPorId(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Produto Teste", response.getBody().getNome());
        verify(produtoService).buscarPorId(id);
    }

    @Test
    void deveSalvarProduto() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Novo Produto");
        request.setDescricao("Descrição do novo produto");
        request.setPreco(25.0);
        request.setCategoriaId(1L);
        request.setEstoqueMinimo(5);

        ProdutoDTO produtoSalvo = ProdutoDTO.builder()
                .id(3L)
                .nome("Novo Produto")
                .descricao("Descrição do novo produto")
                .preco(25.0)
                .estoqueMinimo(5)
                .build();

        when(produtoService.salvar(any(ProdutoRequest.class))).thenReturn(produtoSalvo);

        // Act
        ResponseEntity<ProdutoDTO> response = produtoController.salvar(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(3L, response.getBody().getId());
        assertEquals("Novo Produto", response.getBody().getNome());
        verify(produtoService).salvar(request);
    }

    @Test
    void deveAtualizarProduto() {
        // Arrange
        Long id = 1L;
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Produto Atualizado");
        request.setDescricao("Descrição atualizada");
        request.setPreco(30.0);
        request.setCategoriaId(2L);
        request.setEstoqueMinimo(8);

        ProdutoDTO produtoAtualizado = ProdutoDTO.builder()
                .id(id)
                .nome("Produto Atualizado")
                .descricao("Descrição atualizada")
                .preco(30.0)
                .estoqueMinimo(8)
                .build();

        when(produtoService.atualizar(eq(id), any(ProdutoRequest.class))).thenReturn(produtoAtualizado);

        // Act
        ResponseEntity<ProdutoDTO> response = produtoController.atualizar(id, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().getId());
        assertEquals("Produto Atualizado", response.getBody().getNome());
        verify(produtoService).atualizar(id, request);
    }

    @Test
    void deveExcluirProduto() {
        // Arrange
        Long id = 1L;
        doNothing().when(produtoService).excluir(id);

        // Act
        ResponseEntity<Void> response = produtoController.excluir(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(produtoService).excluir(id);
    }

    @Test
    void deveListarProdutosPorCategoria() {
        // Arrange
        Long categoriaId = 1L;
        List<ProdutoDTO> produtos = Arrays.asList(
                ProdutoDTO.builder().id(1L).nome("Produto 1").preco(10.0).build(),
                ProdutoDTO.builder().id(2L).nome("Produto 2").preco(20.0).build()
        );

        when(produtoService.findByCategoria(categoriaId)).thenReturn(produtos);

        // Act
        ResponseEntity<List<ProdutoDTO>> response = produtoController.listarPorCategoria(categoriaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(produtoService).findByCategoria(categoriaId);
    }

    @Test
    void deveGerarRelatorioDeProdutos() throws IOException {
        // Arrange
        Long categoriaId = 1L;
        byte[] relatorioBytes = "Conteúdo do PDF de teste".getBytes();

        when(relatorioProdutoService.gerarRelatorioProdutosPdf(categoriaId)).thenReturn(relatorioBytes);

        // Act
        ResponseEntity<byte[]> response = produtoController.gerarRelatorioProdutos(categoriaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("relatorio_produtos.pdf"));
        assertEquals(relatorioBytes.length, response.getBody().length);
        verify(relatorioProdutoService).gerarRelatorioProdutosPdf(categoriaId);
    }
}
