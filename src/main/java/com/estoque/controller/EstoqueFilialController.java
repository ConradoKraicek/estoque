package com.estoque.controller;


import com.estoque.dto.*;
import com.estoque.service.EstoqueFilialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque-filial")
@RequiredArgsConstructor
public class EstoqueFilialController {

    private final EstoqueFilialService estoqueFilialService;

    @GetMapping
    public ResponseEntity<List<EstoqueFilialDTO>> listarTodos() {
        return ResponseEntity.ok(estoqueFilialService.listarTodos());
    }

    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<EstoqueFilialDTO>> buscarPorFilial(
            @PathVariable Long filialId) {
        return ResponseEntity.ok(estoqueFilialService.buscarPorFilial(filialId));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<EstoqueFilialDTO>> buscarPorProduto(
            @PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueFilialService.buscarPorProduto(produtoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueFilialDTO> atualizarEstoque(
            @PathVariable Long id,
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(estoqueFilialService.atualizarEstoque(id, quantidade));
    }

    @PostMapping("/transferir")
    public ResponseEntity<Void> transferirEstoque(
            @RequestBody @Valid TransferenciaEstoqueDTO transferencia) {
        estoqueFilialService.transferirEstoque(transferencia);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entrada")
    public ResponseEntity<EstoqueFilialDTO> registrarEntrada(
            @RequestBody @Valid MovimentacaoEstoqueDTO entrada) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estoqueFilialService.registrarEntrada(entrada));
    }

    @PostMapping("/saida")
    public ResponseEntity<EstoqueFilialDTO> registrarSaida(
            @RequestBody @Valid MovimentacaoEstoqueDTO saida) {
        return ResponseEntity.ok(estoqueFilialService.registrarSaida(saida));
    }
}
