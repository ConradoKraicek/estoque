package com.estoque.service;

import com.estoque.exception.ResourceNotFoundException;
import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.ProdutoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public RelatorioProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Gera um relatório de produtos em formato PDF
     * 
     * @param categoriaId ID da categoria para filtrar produtos (opcional)
     * @return Array de bytes contendo o relatório em PDF
     * @throws IOException Se ocorrer um erro ao gerar o relatório
     */
    public byte[] gerarRelatorioProdutosPdf(Long categoriaId) throws IOException {
        try {
            // Obtém os dados dos produtos
            List<Produto> produtos;
            if (categoriaId != null) {
                // Se categoriaId for fornecido, filtra por categoria
                Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
                produtos = produtoRepository.findByCategoria(categoria);
            } else {
                // Caso contrário, obtém todos os produtos
                produtos = produtoRepository.findAll();
            }

            // Carrega o template do relatório
            InputStream jasperStream = new ClassPathResource("reports/produtos.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

            // Cria a fonte de dados para o relatório
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(produtos);

            // Parâmetros do relatório (se necessário)
            Map<String, Object> parameters = new HashMap<>();

            // Preenche o relatório com os dados
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Exporta o relatório para PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

            return baos.toByteArray();
        } catch (JRException e) {
            throw new IOException("Erro ao gerar relatório de produtos", e);
        }
    }
}
