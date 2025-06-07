package com.estoque.mapper;

import com.estoque.dto.ProdutoDTO;
import com.estoque.dto.ProdutoRequest;
import com.estoque.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class})
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    ProdutoDTO toDTO(Produto produto);

    Produto toEntity(ProdutoDTO produtoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    Produto requestToEntity(ProdutoRequest request);
}