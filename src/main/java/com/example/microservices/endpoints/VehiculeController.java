package com.example.microservices.endpoints;

import com.example.microservices.dtos.VehiculeDTO;
import com.example.microservices.model.Vehicule;
import com.example.microservices.services.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@Tag(name = "Vehicles", description = "Vehicle management APIs")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    /**
     * ✅ Ajouter un véhicule à un garage
     */
    @PostMapping("/garages/{garageId}/vehicle")
    @Operation(summary = "Add a vehicle to a garage")
    public ResponseEntity<VehiculeDTO> addVehiculeToGarage(
            @PathVariable UUID garageId,
            @RequestBody VehiculeDTO vehiculeDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehiculeService.addVehiculeToGarage(garageId, vehiculeDTO));
    }

    /**
     * ✅ Modifier un véhicule
     */
    @PutMapping("/vehicles/{id}")
    @Operation(summary = "Update a vehicle")
    public ResponseEntity<VehiculeDTO> updateVehicule(
            @PathVariable UUID id,
            @RequestBody VehiculeDTO vehicule
    ) {
        return ResponseEntity.ok(vehiculeService.updateVehicule(id, vehicule));
    }

    /**
     * ✅ Supprimer un véhicule
     */
    @DeleteMapping("/vehicles/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> deleteVehicule(@PathVariable UUID id) {
        vehiculeService.deleteVehicule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✅ Lister les véhicules d’un garage
     */
    @GetMapping("/garages/{garageId}/vehicles")
    @Operation(summary = "get Vehicles of given garage")
    public Page<VehiculeDTO> getByGarage(
            @PathVariable UUID garageId,
            Pageable pageable
    ) {
        return vehiculeService.getVehiculesByGarage(garageId, pageable);
    }

    /**
     * ✅ Lister par modèle (multi-garages)
     */
    @GetMapping("/vehicles")
    @Operation(summary = "Search vehicles by brand across garages")
    public Page<VehiculeDTO> getByBrand(
            @RequestParam(required = false) String brand,
            Pageable pageable
    ) {
        return vehiculeService.getVehiculesByBrand(brand, pageable);
    }
}