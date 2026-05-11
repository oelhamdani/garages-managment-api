package com.example.microservices.repositories;

import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Garage;
import com.example.microservices.model.Vehicule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Accessoire Repository Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccessoireRepositoryTest {

    @Autowired
    private AccessoireRepository accessoireRepository;

    @Autowired
    private EntityManager entityManager;

    // =========================
    // HELPERS
    // =========================

    private Garage persistGarage(String name) {
        Garage g = new Garage();
        g.setName(name);
        entityManager.persist(g);
        return g;
    }

    private Vehicule persistVehicule(Garage garage, String brand) {
        Vehicule v = new Vehicule();
        v.setBrand(brand);
        v.setTypeCarburant("diesel");
        v.setAnneeFabrication(2020);
        v.setGarage(garage);
        entityManager.persist(v);
        return v;
    }

    private Accessoire persistAccessoire(Vehicule vehicule, String nom, String type) {
        Accessoire a = new Accessoire();
        a.setNom(nom);
        a.setType(type);
        a.setDescription("desc");
        a.setPrix(BigDecimal.TEN);
        a.setVehicule(vehicule);
        entityManager.persist(a);
        return a;
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void should_find_accessoires_by_vehicule_id() {
        Garage g = persistGarage("G1");
        Vehicule v = persistVehicule(g, "BMW");

        persistAccessoire(v, "GPS", "tech");
        persistAccessoire(v, "Camera", "tech");

        entityManager.flush();

        List<Accessoire> result = accessoireRepository.findByVehiculeId(v.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void should_return_empty_when_no_accessoire_for_vehicule() {
        Garage g = persistGarage("G1");
        Vehicule v = persistVehicule(g, "BMW");

        entityManager.flush();

        List<Accessoire> result = accessoireRepository.findByVehiculeId(v.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void should_find_accessoires_by_type() {
        Garage g = persistGarage("G1");
        Vehicule v = persistVehicule(g, "BMW");

        persistAccessoire(v, "GPS", "tech");
        persistAccessoire(v, "Seat", "comfort");

        entityManager.flush();

        List<Accessoire> result = accessoireRepository.findByType("tech");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("GPS");
    }

    @Test
    void should_return_empty_when_type_not_found() {
        Garage g = persistGarage("G1");
        Vehicule v = persistVehicule(g, "BMW");

        persistAccessoire(v, "GPS", "tech");

        entityManager.flush();

        List<Accessoire> result = accessoireRepository.findByType("security");

        assertThat(result).isEmpty();
    }

    @Test
    void should_find_garages_by_accessory_name() {
        Garage g1 = persistGarage("Garage1");
        Garage g2 = persistGarage("Garage2");

        Vehicule v1 = persistVehicule(g1, "BMW");
        Vehicule v2 = persistVehicule(g2, "Audi");

        persistAccessoire(v1, "GPS", "tech");
        persistAccessoire(v2, "Camera", "tech");

        entityManager.flush();

        List<Garage> result = accessoireRepository.findByAccessoryName("GPS");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Garage1");
    }

    @Test
    void should_return_empty_when_accessory_not_found() {
        Garage g1 = persistGarage("Garage1");
        Vehicule v1 = persistVehicule(g1, "BMW");

        persistAccessoire(v1, "Camera", "tech");

        entityManager.flush();

        List<Garage> result = accessoireRepository.findByAccessoryName("GPS");

        assertThat(result).isEmpty();
    }

    @Test
    void should_not_duplicate_garages_when_multiple_matching_accessoires() {
        Garage g1 = persistGarage("Garage1");
        Vehicule v1 = persistVehicule(g1, "BMW");

        persistAccessoire(v1, "GPS", "tech");
        persistAccessoire(v1, "GPS", "tech");

        entityManager.flush();

        List<Garage> result = accessoireRepository.findByAccessoryName("GPS");

        assertThat(result).hasSize(1); // DISTINCT works
    }
}