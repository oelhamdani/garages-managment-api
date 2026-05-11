package com.example.microservices.repositories;

import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface AccessoireRepository extends JpaRepository<Accessoire, UUID> {

    // Retrieve all accessories associated with a specific vehicle
    List<Accessoire> findByVehiculeId(UUID vehiculeId);

    // Retrieve all accessories by type (for filtering purposes)
    List<Accessoire> findByType(String type);

    // Retrieve all garages that have a specific accessory in any of their vehicles


    @Query("""
    SELECT DISTINCT g
    FROM Garage g
    JOIN g.vehicules v
    JOIN v.accessoires a
    WHERE a.nom = :accessoire
    """)
    List<Garage> findByAccessoryName(@Param("accessoire") String accessoire);
}