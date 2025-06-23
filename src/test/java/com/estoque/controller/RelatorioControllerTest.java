package com.estoque.controller;

import com.estoque.service.RelatorioEstoqueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RelatorioControllerTest {

    @Mock
    private RelatorioEstoqueService relatorioEstoqueService;

    @InjectMocks
    private RelatorioController relatorioController;

    @Test
    void deveExportarRelatorioEstoqueEmPDF() throws IOException {
        // Arrange
        Long filialId = 1L;
        String tipo = "pdf";
        byte[] relatorioBytes = "Conteúdo do PDF de teste".getBytes();

        when(relatorioEstoqueService.gerarRelatorioPdf(filialId)).thenReturn(relatorioBytes);

        // Act
        ResponseEntity<byte[]> response = relatorioController.exportarRelatorioEstoque(filialId, tipo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("relatorio_estoque.pdf"));
        assertEquals(relatorioBytes.length, response.getBody().length);
        verify(relatorioEstoqueService).gerarRelatorioPdf(filialId);
    }

    @Test
    void deveExportarRelatorioEstoqueEmExcel() throws IOException {
        // Arrange
        Long filialId = 1L;
        String tipo = "excel";
        byte[] relatorioBytes = "Conteúdo do Excel de teste".getBytes();

        when(relatorioEstoqueService.gerarRelatorioExcel(filialId)).thenReturn(relatorioBytes);

        // Act
        ResponseEntity<byte[]> response = relatorioController.exportarRelatorioEstoque(filialId, tipo);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                     response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("relatorio_estoque.xlsx"));
        assertEquals(relatorioBytes.length, response.getBody().length);
        verify(relatorioEstoqueService).gerarRelatorioExcel(filialId);
    }
}
