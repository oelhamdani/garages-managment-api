package com.example.microservices.services;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.exception.AccessoireNotFoundException;
import com.example.microservices.exception.VehiculeNotFoundException;
import com.example.microservices.mapping.AccessoireMapper;
import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Vehicule;
import com.example.microservices.repositories.AccessoireRepository;
import com.example.microservices.repositories.VehiculeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AccessoireServiceImpl implements AccessoireService {

    private final AccessoireRepository accessoireRepository;
    private final VehiculeRepository vehiculeRepository;
    private final AccessoireMapper accessoireMapper;

    public AccessoireServiceImpl(AccessoireRepository accessoireRepository,
                                 VehiculeRepository vehiculeRepository,
                                 AccessoireMapper accessoireMapper) {
        this.accessoireRepository = accessoireRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.accessoireMapper = accessoireMapper;
    }

    @Override
    public AccessoireDTO addAccessoireToVehicule(UUID vehiculeId, AccessoireDTO accessoireDTO) {

        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new AccessoireNotFoundException("Vehicle not found"));

        validateAccessoire(accessoireDTO);

        // DTO -> Entity
        Accessoire accessoire = accessoireMapper.toEntity(accessoireDTO);

        // Set relation
        accessoire.setVehicule(vehicule);

        // Save
        Accessoire saved = accessoireRepository.save(accessoire);
        log.info("Accessoire successfully saved {} ", saved);
        // Entity -> DTO
        return accessoireMapper.toDto(saved);
    }

    @Override
    public AccessoireDTO updateAccessoire(UUID accessoireId, AccessoireDTO accessoireDTO) {

        Accessoire existing = accessoireRepository.findById(accessoireId)
                .orElseThrow(() -> new AccessoireNotFoundException("Accessoire not found"));

        validateAccessoire(accessoireDTO);

        // MapStruct update instead of setters
        accessoireMapper.updateEntityFromDto(accessoireDTO, existing);

        Accessoire updated = accessoireRepository.save(existing);
        log.info("Accessoire successfully updated {} ", updated);
        return accessoireMapper.toDto(updated);
    }

    @Override
    public void deleteAccessoire(UUID accessoireId) {

        Accessoire existing = accessoireRepository.findById(accessoireId)
                .orElseThrow(() -> new AccessoireNotFoundException("Accessoire not found"));

        accessoireRepository.delete(existing);
    }

    @Override
    public List<AccessoireDTO> getAccessoiresByVehicule(UUID vehiculeId) {

        if (!vehiculeRepository.existsById(vehiculeId)) {
            throw new VehiculeNotFoundException("Vehicle not found");
        }

        return accessoireRepository.findByVehiculeId(vehiculeId)
                .stream()
                .map(accessoireMapper::toDto)
                .toList();
    }

    private void validateAccessoire(AccessoireDTO accessoireDTO) {
        if (accessoireDTO.getNom() == null || accessoireDTO.getNom().isBlank()) {
            throw new IllegalArgumentException("Nom is required");
        }
        if (accessoireDTO.getType() == null || accessoireDTO.getType().isBlank()) {
            throw new IllegalArgumentException("Type is required");
        }
        if (accessoireDTO.getDescription() == null || accessoireDTO.getDescription().isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
        if (accessoireDTO.getPrix() == null) {
            throw new IllegalArgumentException("price is required");
        }
    }
}