package com.example.microservices.mapping;

import com.example.microservices.dtos.VehiculeDTO;
import com.example.microservices.model.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel ="spring")
public interface VehiculeMapper {

    VehiculeDTO toDto(Vehicule vehicule);

    Vehicule toEntity(VehiculeDTO vehiculeDTO);

    void updateEntityFromDto(VehiculeDTO vehiculeDTO, @MappingTarget Vehicule vehicule);
}
