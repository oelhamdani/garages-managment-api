package com.example.microservices.integrationtests;

import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.model.Garage;
import com.example.microservices.repositories.GarageRepository;
import com.example.microservices.services.GarageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Use application-test.yml for test configuration
@Transactional
class GarageServiceImplIntegrationTest {

    @Autowired
    private GarageServiceImpl garageService;

    @Autowired
    private GarageRepository garageRepository;

    @Test
    void createGarage_success() {
        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setName("Test Garage");
        garageDTO.setAddress("123 Test St");
        garageDTO.setTelephone("123456789");
        garageDTO.setEmail("test@example.com");
        GarageDTO createdGarage = garageService.createGarage(garageDTO);
        assertNotNull(createdGarage);
        assertEquals("Test Garage", createdGarage.getName());
        assertTrue(garageRepository.existsById(createdGarage.getId()));
    }

    @Test
    void deleteGarage_success() {
        Garage garage = new Garage();

        garage.setName("Garage to Delete");
        garageRepository.save(garage);

        garageService.deleteGarage(garage.getId());

        assertFalse(garageRepository.existsById(garage.getId()));
    }
}