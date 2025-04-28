package com.estoque.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueFilialDTO {
    private Long id;
    private Long filialId;
    private String filialNome;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Integer estoqueMinimo;
}
