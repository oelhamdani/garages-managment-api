package com.example.microservices.repositories;

import com.example.microservices.model.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VehiculeRepository extends JpaRepository<Vehicule, UUID> {

    // Retrieve all vehicles associated with a specific garage
    Page<Vehicule> findByGarageId(UUID garageId, Pageable pageable);

    // Retrieve all vehicles of a given model (brand) across all garages
    @Query("SELECT v FROM Vehicule v WHERE v.brand = :brand")
    Page<Vehicule> findByBrandAcrossGarages(@Param("brand") String brand, Pageable pageable);

    // Count the number of vehicles in a specific garage
    @Query("SELECT COUNT(v) FROM Vehicule v WHERE v.garage.id = :garageId")
    long countByGarageId(@Param("garageId") UUID garageId);

}