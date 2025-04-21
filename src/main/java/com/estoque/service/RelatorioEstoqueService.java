package com.estoque.service;



import com.estoque.model.EstoqueFilial;
import com.estoque.repository.EstoqueFilialRepository;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RelatorioEstoqueService {

    private final EstoqueFilialRepository estoqueFilialRepository;

    public RelatorioEstoqueService(EstoqueFilialRepository estoqueFilialRepository) {
        this.estoqueFilialRepository = estoqueFilialRepository;
    }

    public List<EstoqueFilial> gerarDadosRelatorio(Long filialId) {
        if (filialId != null) {
            return estoqueFilialRepository.findByFilialId(filialId);
        }
        return estoqueFilialRepository.findAll();
    }

    public byte[] gerarRelatorioExcel(Long filialId) throws IOException {
        List<EstoqueFilial> estoques = gerarDadosRelatorio(filialId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Estoque");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Produto", "Filial", "Quantidade", "Estoque Mínimo", "Status"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Dados
            int rowNum = 1;
            for (EstoqueFilial estoque : estoques) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(estoque.getProduto().getNome());
                row.createCell(1).setCellValue(estoque.getFilial().getNome());
                row.createCell(2).setCellValue(estoque.getQuantidade());
                row.createCell(3).setCellValue(estoque.getProduto().getEstoqueMinimo());
                row.createCell(4).setCellValue(getStatusEstoque(estoque));
            }

            // Auto size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }


    public byte[] gerarRelatorioPdf(Long filialId) throws IOException {
        List<EstoqueFilial> estoques = gerarDadosRelatorio(filialId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Relatório de Estoque", (com.itextpdf.text.Font) titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Tabela
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Cabeçalho da tabela
            String[] headers = {"Produto", "Filial", "Quantidade", "Estoque Mínimo", "Status"};
            Font headerFont = (Font) FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, (com.itextpdf.text.Font) headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            // Dados
            Font dataFont = (Font) FontFactory.getFont(FontFactory.HELVETICA, 10);

            for (EstoqueFilial estoque : estoques) {
                table.addCell(new Phrase(estoque.getProduto().getNome(), (com.itextpdf.text.Font) dataFont));
                table.addCell(new Phrase(estoque.getFilial().getNome(), (com.itextpdf.text.Font) dataFont));
                table.addCell(new Phrase(String.valueOf(estoque.getQuantidade()), (com.itextpdf.text.Font) dataFont));
                table.addCell(new Phrase(String.valueOf(estoque.getProduto().getEstoqueMinimo()), (com.itextpdf.text.Font) dataFont));

                PdfPCell statusCell = new PdfPCell(new Phrase(getStatusEstoque(estoque), (com.itextpdf.text.Font) dataFont));
                switch (getStatusEstoque(estoque)) {
                    case "ESGOTADO":
                        statusCell.setBackgroundColor(BaseColor.RED);
                        break;
                    case "ESTOQUE BAIXO":
                        statusCell.setBackgroundColor(BaseColor.ORANGE);
                        break;
                    default:
                        statusCell.setBackgroundColor(BaseColor.GREEN);
                }
                table.addCell(statusCell);
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new IOException("Erro ao gerar PDF", e);
        }
    }

    private String getStatusEstoque(EstoqueFilial estoque) {
        if (estoque.getQuantidade() == 0) {
            return "ESGOTADO";
        } else if (estoque.getQuantidade() < estoque.getProduto().getEstoqueMinimo()) {
            return "ESTOQUE BAIXO";
        } else {
            return "OK";
        }
    }
}
