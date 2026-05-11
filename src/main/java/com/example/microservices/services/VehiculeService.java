package com.example.microservices.services;

import com.example.microservices.dtos.VehiculeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VehiculeService {

    VehiculeDTO addVehiculeToGarage(UUID garageId, VehiculeDTO vehiculeDTO);

    VehiculeDTO updateVehicule(UUID vehiculeId, VehiculeDTO vehiculeDTO);

    void deleteVehicule(UUID vehiculeId);

    Page<VehiculeDTO> getVehiculesByGarage(UUID garageId, Pageable pageable);

    Page<VehiculeDTO> getVehiculesByBrand(String brand, Pageable pageable);

    void handleVehiculeCreatedEvent(String message);
}