package com.estoque.mapper;


import com.estoque.dto.EstoqueFilialDTO;
import com.estoque.model.EstoqueFilial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EstoqueFilialMapper {

    EstoqueFilialMapper INSTANCE = Mappers.getMapper(EstoqueFilialMapper.class);

    @Mapping(target = "filialId", source = "filial.id")
    @Mapping(target = "filialNome", source = "filial.nome")
    @Mapping(target = "produtoId", source = "produto.id")
    @Mapping(target = "produtoNome", source = "produto.nome")
    @Mapping(target = "estoqueMinimo", source = "produto.estoqueMinimo")
    EstoqueFilialDTO toDTO(EstoqueFilial estoqueFilial);
}
