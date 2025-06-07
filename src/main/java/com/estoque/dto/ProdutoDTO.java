package com.estoque.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String codigoBarras;
    private Double preco;
    private CategoriaDTO categoria;
    private Integer estoqueMinimo;
}