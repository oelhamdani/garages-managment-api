package com.example.microservices.mapping;

import com.example.microservices.dtos.OpeningTimeDTO;
import com.example.microservices.model.OpeningTime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {
    OpeningTimeDTO toDto(OpeningTime openingTime);

    OpeningTime toEntity(OpeningTimeDTO openingTimeDTO);
}