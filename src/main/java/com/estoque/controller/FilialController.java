package com.estoque.controller;


import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.service.FilialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filiais")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;

    @GetMapping
    public ResponseEntity<List<FilialDTO>> listarTodas() {
        return ResponseEntity.ok(filialService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(filialService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FilialDTO> criar(@RequestBody @Valid FilialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filialService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilialDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FilialRequest request) {
        return ResponseEntity.ok(filialService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        filialService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
