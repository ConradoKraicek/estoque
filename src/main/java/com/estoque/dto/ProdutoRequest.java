package com.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @Size(max = 50, message = "Código de barras deve ter no máximo 50 caracteres")
    private String codigoBarras;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private Double preco;

    private Long categoriaId;

    @NotNull(message = "Estoque mínimo é obrigatório")
    @Positive(message = "Estoque mínimo deve ser maior que zero")
    private Integer estoqueMinimo;
}