package com.estoque.repository;

import com.estoque.model.Categoria;
import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    boolean existsByCodigoBarras(String codigoBarras);

    List<Produto> findByCategoria(Categoria categoria);
}
