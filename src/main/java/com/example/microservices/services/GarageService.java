package com.example.microservices.services;

import com.example.microservices.dtos.GarageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GarageService {
    GarageDTO createGarage(GarageDTO garageDto);
    GarageDTO updateGarage(UUID id, GarageDTO garageDTO);
    void deleteGarage(UUID id);
    GarageDTO getGarageById(UUID id);
    Page<GarageDTO> getAllGarages(Pageable pageable);
    Page<GarageDTO> searchGarages(String typeVehicule, String accessoire, Pageable pageable);
}