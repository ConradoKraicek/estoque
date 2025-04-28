package com.estoque.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferenciaEstoqueDTO {
    @NotNull(message = "ID da filial de origem é obrigatório")
    private Long origemId;

    @NotNull(message = "ID da filial de destino é obrigatório")
    private Long destinoId;

    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;
}
