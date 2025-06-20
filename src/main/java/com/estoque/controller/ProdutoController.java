package com.estoque.controller;

import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.model.Categoria;
import com.estoque.service.CategoriaService;
import com.estoque.service.ProdutoService;
import com.estoque.service.RelatorioProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final RelatorioProdutoService relatorioProdutoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @GetMapping("/por-categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(produtoService.findByCategoria(categoriaId));
    }

    /**
     * Gera um relatório de produtos em formato PDF
     * 
     * @param categoriaId ID da categoria para filtrar produtos (opcional)
     * @return Relatório em formato PDF
     * @throws IOException Se ocorrer um erro ao gerar o relatório
     */
    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorioProdutos(@RequestParam(required = false) Long categoriaId) throws IOException {
        byte[] relatorio = relatorioProdutoService.gerarRelatorioProdutosPdf(categoriaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_produtos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(relatorio);
    }
}
