package com.example.microservices.repositories;

import com.example.microservices.model.Garage;
import com.example.microservices.model.Vehicule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import jakarta.persistence.EntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Vehicule Repository Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VehiculeRepositoryTest {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private EntityManager entityManager;

    // =========================
    // HELPERS
    // =========================

    private Garage persistGarage(String name) {
        Garage garage = new Garage();
        garage.setName(name);
        entityManager.persist(garage);
        return garage;
    }

    private Vehicule persistVehicule(Garage garage, String brand, String fuel) {
        Vehicule v = new Vehicule();
        v.setBrand(brand);
        v.setTypeCarburant(fuel);
        v.setAnneeFabrication(2020);
        v.setGarage(garage);
        entityManager.persist(v);
        return v;
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void should_find_vehicles_by_garage_id() {
        Garage g1 = persistGarage("G1");
        Garage g2 = persistGarage("G2");

        persistVehicule(g1, "BMW", "diesel");
        persistVehicule(g1, "Audi", "petrol");
        persistVehicule(g2, "Tesla", "electric");

        entityManager.flush();

        Page<Vehicule> result = vehiculeRepository.findByGarageId(
                g1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void should_return_empty_when_no_vehicle_for_garage() {
        Garage g1 = persistGarage("G1");

        entityManager.flush();

        Page<Vehicule> result = vehiculeRepository.findByGarageId(
                g1.getId(),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void should_find_by_brand_across_garages() {
        Garage g1 = persistGarage("G1");
        Garage g2 = persistGarage("G2");

        persistVehicule(g1, "BMW", "diesel");
        persistVehicule(g2, "BMW", "petrol");
        persistVehicule(g2, "Audi", "diesel");

        entityManager.flush();

        Page<Vehicule> result = vehiculeRepository.findByBrandAcrossGarages(
                "BMW",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void should_return_empty_when_brand_not_found() {
        Garage g1 = persistGarage("G1");

        persistVehicule(g1, "Audi", "diesel");

        entityManager.flush();

        Page<Vehicule> result = vehiculeRepository.findByBrandAcrossGarages(
                "BMW",
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void should_count_vehicles_by_garage() {
        Garage g1 = persistGarage("G1");

        persistVehicule(g1, "BMW", "diesel");
        persistVehicule(g1, "Audi", "petrol");

        entityManager.flush();

        long count = vehiculeRepository.countByGarageId(g1.getId());

        assertThat(count).isEqualTo(2);
    }

    @Test
    void should_return_zero_when_no_vehicles() {
        Garage g1 = persistGarage("G1");

        entityManager.flush();

        long count = vehiculeRepository.countByGarageId(g1.getId());

        assertThat(count).isZero();
    }
}