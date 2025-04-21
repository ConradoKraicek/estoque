package com.estoque.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Filial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String endereco;

    private String telefone;

    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL)
    private List<EstoqueFilial> estoques = new ArrayList<>();
}
