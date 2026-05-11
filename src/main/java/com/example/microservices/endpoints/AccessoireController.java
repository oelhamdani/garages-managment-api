package com.example.microservices.endpoints;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Vehicule;
import com.example.microservices.services.AccessoireService;
import com.example.microservices.services.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Accessoires", description = "Accessoire management APIs")
public class AccessoireController {

    @Autowired
    private AccessoireService accessoireService;

    /**
     * ✅ Ajouter un accessoire à une vehicule
     */
    @PostMapping("/vehicules/{vehiculeId}/accessoire")
    @Operation(summary = "Add accessory to a vehicule")
    public ResponseEntity<AccessoireDTO> addAccessoireToVehicule(
            @PathVariable UUID vehiculeId,
            @RequestBody AccessoireDTO accessoireDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accessoireService.addAccessoireToVehicule(vehiculeId, accessoireDTO));
    }

    /**
     * ✅ Modifier un Accessoire
     */
    @PutMapping("/accessoires/{id}")
    @Operation(summary = "Update an accessory")
    public ResponseEntity<AccessoireDTO> updateAccessoire(
            @PathVariable UUID id,
            @RequestBody AccessoireDTO accessoireDTO
    ) {
        return ResponseEntity.ok(accessoireService.updateAccessoire(id, accessoireDTO));
    }

    /**
     * ✅ Supprimer un accessoire
     */
    @DeleteMapping("/accessoires/{id}")
    @Operation(summary = "Delete an accessory")
    public ResponseEntity<Void> deleteAcessoire(@PathVariable UUID id) {
        accessoireService.deleteAccessoire(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ✅ Lister les accessoires d’une vehicule
     */
    @GetMapping("/vehicules/{vehiculeId}/accessoire")
    @Operation(summary = "List accessories of a vehicule")
    public List<AccessoireDTO> getByVehicule(
            @PathVariable UUID vehiculeId,
            Pageable pageable
    ) {
        return accessoireService.getAccessoiresByVehicule(vehiculeId);
    }

}