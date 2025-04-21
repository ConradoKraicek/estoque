package com.estoque.controller;


import com.estoque.service.RelatorioEstoqueService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioEstoqueService relatorioEstoqueService;

    public RelatorioController(RelatorioEstoqueService relatorioEstoqueService) {
        this.relatorioEstoqueService = relatorioEstoqueService;
    }

    @GetMapping("/estoque/exportar")
    public ResponseEntity<byte[]> exportarRelatorioEstoque(@RequestParam(required = false) Long filialId, @RequestParam String tipo) throws IOException {

        byte[] relatorio;
        String contentType;
        String fileName;

        if ("pdf".equalsIgnoreCase(tipo)) {
            relatorio = relatorioEstoqueService.gerarRelatorioPdf(filialId);
            contentType = MediaType.APPLICATION_PDF_VALUE;
            fileName = "relatorio_estoque.pdf";
        } else {
            relatorio = relatorioEstoqueService.gerarRelatorioExcel(filialId);
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            fileName = "relatorio_estoque.xlsx";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(relatorio);
    }
}
