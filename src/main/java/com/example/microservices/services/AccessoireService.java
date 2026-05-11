package com.example.microservices.services;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.model.Accessoire;

import java.util.List;
import java.util.UUID;

public interface AccessoireService {

    AccessoireDTO addAccessoireToVehicule(UUID vehiculeId, AccessoireDTO accessoireDTO);

    AccessoireDTO updateAccessoire(UUID accessoireId, AccessoireDTO accessoireDTO);

    void deleteAccessoire(UUID accessoireId);

    List<AccessoireDTO> getAccessoiresByVehicule(UUID vehiculeId);
}