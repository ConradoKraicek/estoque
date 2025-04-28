package com.estoque.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentacaoEstoqueDTO {
    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "ID da filial é obrigatório")
    private Long filialId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;
}
