package com.estoque.repository;

import com.estoque.model.EstoqueFilial;
import com.estoque.model.Filial;
import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueFilialRepository extends JpaRepository<EstoqueFilial, Long> {

    Optional<EstoqueFilial> findByFilialAndProduto(Filial filial, Produto produto);

    List<EstoqueFilial> findByFilial(Filial filial);

    @Query("SELECT e FROM EstoqueFilial e WHERE e.filial.id = :filialId")
    List<EstoqueFilial> findByFilialId(@Param("filialId") Long filialId);

    @Query("SELECT e FROM EstoqueFilial e JOIN FETCH e.produto JOIN FETCH e.filial")
    List<EstoqueFilial> findAllWithProdutoAndFilial();



    @Query("SELECT e FROM EstoqueFilial e WHERE e.produto.id = :produtoId")
    List<EstoqueFilial> findByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT e FROM EstoqueFilial e WHERE e.filial.id = :filialId AND e.produto.id = :produtoId")
    Optional<EstoqueFilial> findByFilialIdAndProdutoId(@Param("filialId") Long filialId, @Param("produtoId") Long produtoId);
}
