package com.estoque.mapper;


import com.estoque.dto.FilialDTO;
import com.estoque.dto.FilialRequest;
import com.estoque.model.Filial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FilialMapper {

    FilialMapper INSTANCE = Mappers.getMapper(FilialMapper.class);

    FilialDTO toDTO(Filial filial);

    Filial toEntity(FilialDTO filialDTO);

    @Mapping(target = "id", ignore = true)
    Filial requestToEntity(FilialRequest request);
}
