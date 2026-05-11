package com.example.microservices.repositories;

import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Garage;
import com.example.microservices.model.Vehicule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GarageRepositoryTest {

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private AccessoireRepository accessoireRepository;

    @Test
    @DisplayName("Should find garage by vehicle type")
    void should_find_garage_by_vehicle_type() {

        // GIVEN
        Garage garage = new Garage();
        garage.setName("Garage A");
        garage = garageRepository.save(garage);

        Vehicule vehicule = new Vehicule();
        vehicule.setBrand("Toyota");
        vehicule.setTypeCarburant("Diesel");
        vehicule.setGarage(garage);
        vehiculeRepository.save(vehicule);

        // WHEN
        Page<Garage> result = garageRepository.searchGarages(
                "Diesel",
                null,
                PageRequest.of(0, 10)
        );

        // THEN
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Garage A");
    }

    @Test
    @DisplayName("Should find garage by accessory name")
    void should_find_garage_by_accessory() {

        // GIVEN
        Garage garage = new Garage();
        garage.setName("Garage B");
        garage = garageRepository.save(garage);

        Vehicule vehicule = new Vehicule();
        vehicule.setBrand("BMW");
        vehicule.setTypeCarburant("Petrol");
        vehicule.setGarage(garage);
        vehicule = vehiculeRepository.save(vehicule);

        Accessoire accessoire = new Accessoire();
        accessoire.setNom("GPS");
        accessoire.setVehicule(vehicule);
        accessoireRepository.save(accessoire);

        // WHEN
        Page<Garage> result = garageRepository.searchGarages(
                null,
                "GPS",
                PageRequest.of(0, 10)
        );

        // THEN
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Garage B");
    }

    @Test
    @DisplayName("Should return empty when no match")
    void should_return_empty_when_no_match() {

        Page<Garage> result = garageRepository.searchGarages(
                "Electric",
                "NonExistingAccessory",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).isEmpty();
    }
}