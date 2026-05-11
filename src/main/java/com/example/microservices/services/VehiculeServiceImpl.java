package com.example.microservices.services;

import com.example.microservices.dtos.VehiculeDTO;
import com.example.microservices.exception.GarageBusinessException;
import com.example.microservices.exception.VehiculeNotFoundException;
import com.example.microservices.kafka.producer.VehiculePublisher;
import com.example.microservices.mapping.VehiculeMapper;
import com.example.microservices.model.Garage;
import com.example.microservices.model.Vehicule;
import com.example.microservices.repositories.GarageRepository;
import com.example.microservices.repositories.VehiculeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class VehiculeServiceImpl implements VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehiculePublisher vehiculePublisher;
    @Autowired
    private VehiculeMapper vehiculeMapper;
    private static final int MAX_VEHICLES_PER_GARAGE = 50;

    @Override
    public VehiculeDTO addVehiculeToGarage(UUID garageId, VehiculeDTO vehiculeDTO) {
        // Log the start of the operation
        log.info("Attempting to add vehicle to garage with ID: {}", garageId);

        // Retrieve the Garage by its ID or throw a custom exception if not found
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new GarageBusinessException("Garage with ID " + garageId + " not found"));

        // Check if the garage has reached the maximum capacity of vehicles
        checkGarageCapacity(garageId);

        // Validate the vehicle data (DTO)
        validateVehicle(vehiculeDTO);

        // Set the Garage ID in the Vehicle DTO
        vehiculeDTO.setGarageId(garage.getId());

        // Convert the Vehicle DTO to an entity
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);

        // Save the vehicle to the repository
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        // Publish a Kafka event after the vehicle is saved
        vehiculePublisher.publishVehiculeCreation(savedVehicule);

        // Log the successful vehicle addition
        log.info("Vehicle with ID: {} added to garage with ID: {}", savedVehicule.getId(), garageId);

        // Return the saved vehicle as a DTO
        return vehiculeMapper.toDto(savedVehicule);
    }

    public void handleVehiculeCreatedEvent(String message) {
        log.info("Handling Kafka event: {}", message);

        // Example: deserialize
        try {
            Vehicule vehicule = new ObjectMapper().readValue(message, Vehicule.class);

            // do something useful here
            log.info("Vehicule received: {}", vehicule.getId());

        } catch (Exception e) {
            log.error("Failed to process Kafka message", e);
            throw new RuntimeException(e);
        }
    }

    // Helper method to check the capacity of a garage
    private void checkGarageCapacity(UUID garageId) {
        long count = vehiculeRepository.countByGarageId(garageId);
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            log.error("Garage with ID: {} has reached its maximum capacity of {} vehicles", garageId, MAX_VEHICLES_PER_GARAGE);
            throw new GarageBusinessException("Garage has reached the maximum capacity of " + MAX_VEHICLES_PER_GARAGE + " vehicles.");
        }
    }

    @Override
    public VehiculeDTO updateVehicule(UUID vehiculeId, VehiculeDTO vehiculeDTO) {
        log.info("attempting to update vehicle  : {}", vehiculeId);
        Vehicule existingVehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new VehiculeNotFoundException("Vehicle not found"));

        validateVehicle(vehiculeDTO);
        vehiculeMapper.updateEntityFromDto(vehiculeDTO, existingVehicule);
        vehiculeRepository.save(existingVehicule);
        log.info("Vehicle successfully updated : {}", existingVehicule);
        return vehiculeMapper.toDto(existingVehicule);

    }

    @Override
    public void deleteVehicule(UUID vehiculeId) {

        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new VehiculeNotFoundException("Vehicle not found");
        }

        vehiculeRepository.deleteById(vehiculeId);
    }

    @Override
    public Page<VehiculeDTO> getVehiculesByGarage(UUID garageId, Pageable pageable) {

        if (!garageRepository.existsById(garageId)) {
            throw new GarageBusinessException("Garage not found");
        }
         Page<Vehicule> vehicles = vehiculeRepository.findByGarageId(garageId, pageable);

        return vehicles.map(vehicule -> vehiculeMapper.toDto(vehicule));
    }

    @Override
    public Page<VehiculeDTO> getVehiculesByBrand(String brand, Pageable pageable) {

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand is required");
        }
        Page<Vehicule> vehicles = vehiculeRepository.findByBrandAcrossGarages(brand, pageable);
        return vehicles.map(vehiculeMapper::toDto);
    }

    private void validateVehicle(VehiculeDTO vehiculeDTO) {
        if (vehiculeDTO.getBrand() == null || vehiculeDTO.getBrand().isBlank()) {
            throw new IllegalArgumentException("Brand is required");
        }
        if (vehiculeDTO.getAnneeFabrication() <= 0) {
            throw new IllegalArgumentException("Invalid year");
        }
        if (vehiculeDTO.getTypeCarburant() == null || vehiculeDTO.getTypeCarburant().isBlank()) {
            throw new IllegalArgumentException("Fuel type is required");
        }
    }
}