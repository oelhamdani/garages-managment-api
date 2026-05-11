package com.example.microservices.repositories;

import com.example.microservices.model.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface GarageRepository extends JpaRepository<Garage, UUID> {
    // Standard CRUD operations are inherited from JpaRepository
    // Custom query for searching garages based on vehicle type and accessory availability
    @Query("""
    SELECT DISTINCT g
    FROM Garage g
    JOIN g.vehicules v
    LEFT JOIN v.accessoires a
    WHERE (:typeVehicule IS NULL OR v.typeCarburant = :typeVehicule)
    AND (:accessoire IS NULL OR a.nom = :accessoire)
    """)
    Page<Garage> searchGarages(
            @Param("typeVehicule") String typeVehicule,
            @Param("accessoire") String accessoire,
            Pageable pageable
    );
}