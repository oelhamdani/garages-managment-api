package com.example.microservices.mapping;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.model.Accessoire;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccessoireMapper {

    AccessoireDTO toDto(Accessoire accessoire);

    Accessoire toEntity(AccessoireDTO accessoireDTO);
    void updateEntityFromDto(AccessoireDTO dto, @MappingTarget Accessoire entity);
}