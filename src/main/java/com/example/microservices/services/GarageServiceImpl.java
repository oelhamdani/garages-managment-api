package com.example.microservices.services;

import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.exception.GarageBusinessException;
import com.example.microservices.mapping.GarageMapper;
import com.example.microservices.mapping.OpeningTimeMapper;
import com.example.microservices.model.Garage;
import com.example.microservices.model.OpeningTime;
import com.example.microservices.repositories.AccessoireRepository;
import com.example.microservices.repositories.GarageRepository;
import com.example.microservices.repositories.VehiculeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GarageServiceImpl implements GarageService {

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private AccessoireRepository accessoireRepository;
    @Autowired
    private GarageMapper garageMapper;
    @Autowired
    private OpeningTimeMapper openingTimeMapper;

    @Transactional
    public GarageDTO createGarage(GarageDTO garageDto) {
        // Validate the garage DTO
        validateGarageInfo(garageDto);

        try {
            // Convert GarageDTO to Garage entity
            Garage garageEntity = garageMapper.toEntity(garageDto);

            // Save the garage entity to the repository
            Garage savedGarage = garageRepository.save(garageEntity);

            // Convert the saved Garage entity back to DTO and return
            GarageDTO garageDTO = garageMapper.toDto(savedGarage);

            return garageDTO;
        } catch (Exception e) {
            // Log the exception (you can add logging with SLF4J or other frameworks)
            log.error("Error occurred while creating Garage: ", e);

            // Optionally throw a custom exception or wrap the exception
            throw new RuntimeException("Error while creating Garage", e);
        }
    }

    @Override
    @Transactional
    public GarageDTO updateGarage(UUID id, GarageDTO garageDTO) {
        // Check if the garage exists
       Garage existingGarage = garageRepository.findById(id).orElseThrow(()->{
            log.error("Garage with ID {} not found", id);
            return new GarageBusinessException("Garage with ID " + id + " not found");
        });

        // Validate the updated garage
        validateGarageInfo(garageDTO);

        // Update the existing garage
        garageMapper.updateEntityFromDto(garageDTO, existingGarage);
        // manually map horairesOuverture
        if (garageDTO.getHorairesOuverture() != null) {

            Map<DayOfWeek, List<OpeningTime>> mapped = garageDTO.getHorairesOuverture()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().stream()
                                    .map(openingTimeMapper::toEntity)
                                    .toList()
                    ));

            existingGarage.setHorairesOuverture(mapped);
        }
        Garage garage = garageRepository.save(existingGarage);
        // Log success and return the updated DTO
        log.info("Garage with ID {} updated successfully", id);
        return garageMapper.toDto(garage);
    }

    @Override
    @Transactional
    public void deleteGarage(UUID id) {
        // Ensure the garage exists before deletion
        if (!garageRepository.existsById(id)) {
            throw new IllegalArgumentException("Garage not found");
        }

        // Check if there are vehicles in the garage before deletion
        long vehicleCount = vehiculeRepository.countByGarageId(id);
        if (vehicleCount > 0) {
            throw new IllegalStateException("Garage contains vehicles. Please remove them before deleting the garage.");
        }

        // Delete the garage
        garageRepository.deleteById(id);
    }

    @Override
    public GarageDTO getGarageById(UUID id) {
        Garage garageFound = garageRepository.findById(id).orElseThrow(()->{
            log.error("Garage with ID {} not found", id);
            return new GarageBusinessException("Garage with ID " + id + " not found");
        });

        return garageMapper.toDto(garageFound);
    }

    @Override
    public Page<GarageDTO> getAllGarages(Pageable pageable) {
        Page<Garage> garages = garageRepository.findAll(pageable);
        return garages.map(garageMapper::toDto);
    }

    @Override
    public Page<GarageDTO> searchGarages(String typeVehicule, String accessoire, Pageable pageable) {
        // Get a Page of Garage entities from the repository based on the search criteria
        Page<Garage> garages = garageRepository.searchGarages(typeVehicule, accessoire, pageable);

        // Convert Page<Garage> to Page<GarageDTO> using MapStruct mapper
        return garages.map(garageMapper::toDto);
    }

    // Helper method to validate garage details
    private void validateGarageInfo(GarageDTO garageDto) {
        if (garageDto.getName() == null || garageDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Garage name is required");
        }
        if (garageDto.getAddress() == null || garageDto.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Garage address is required");
        }
        if (garageDto.getTelephone() == null || garageDto.getTelephone().isEmpty()) {
            throw new IllegalArgumentException("Garage telephone is required");
        }
        if (garageDto.getEmail() == null || garageDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Garage email is required");
        }
    }

    // Helper method to check if a garage has reached the vehicle limit
    private void checkVehicleLimit(UUID garageId) {
        long vehicleCount = vehiculeRepository.countByGarageId(garageId);
        if (vehicleCount >= 50) {
            throw new IllegalStateException("Garage has reached its vehicle storage limit of 50.");
        }
    }
}