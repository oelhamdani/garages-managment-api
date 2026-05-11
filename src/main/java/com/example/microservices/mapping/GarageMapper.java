package com.example.microservices.mapping;


import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.model.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = OpeningTimeMapper.class)
public interface GarageMapper {
    @Mapping(target = "horairesOuverture", ignore = true)
    GarageDTO toDto(Garage garage);
    @Mapping(target = "horairesOuverture", ignore = true)
    Garage toEntity(GarageDTO garageDTO);

    // Update existing entity with values from DTO
    @Mapping(target = "horairesOuverture", ignore = true)
    void updateEntityFromDto(GarageDTO garageDTO, @MappingTarget Garage garage);
}
