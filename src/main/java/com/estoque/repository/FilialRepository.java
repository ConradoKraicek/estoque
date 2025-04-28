package com.estoque.repository;

import com.estoque.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    boolean existsByNome(String nome);
}
