package com.example.microservices.endpoints;

import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.model.Garage;
import com.example.microservices.services.GarageService;
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
@RequestMapping("/garages")
@Tag(name = "Garages", description = "Garage management APIs")
public class GarageController {

    @Autowired
    private GarageService garageService;

    @PostMapping
    @Operation(summary = "Create a garage")
    public ResponseEntity<GarageDTO> createGarage(@RequestBody GarageDTO garageDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(garageService.createGarage(garageDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a garage")
    public ResponseEntity<GarageDTO> updateGarage(@PathVariable UUID id, @RequestBody GarageDTO garageDto) {
        return ResponseEntity.ok(garageService.updateGarage(id, garageDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a garage")
    public ResponseEntity<Void> deleteGarage(@PathVariable UUID id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a garage")
    public ResponseEntity<GarageDTO> getGarage(@PathVariable UUID id) {
        return ResponseEntity.ok(garageService.getGarageById(id));
    }

    @GetMapping
    @Operation(summary = "Get all garages")
    public ResponseEntity<Page<GarageDTO>> getAllGarages(Pageable pageable) {
        return ResponseEntity.ok(garageService.getAllGarages(pageable));
    }
}