package com.estoque.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilialDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
}
