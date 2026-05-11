package com.example.microservices.services;

import com.example.microservices.dtos.VehiculeDTO;
import com.example.microservices.exception.GarageBusinessException;
import com.example.microservices.exception.VehiculeNotFoundException;
import com.example.microservices.kafka.producer.VehiculePublisher;
import com.example.microservices.mapping.VehiculeMapper;
import com.example.microservices.model.Garage;
import com.example.microservices.model.Vehicule;
import com.example.microservices.repositories.GarageRepository;
import com.example.microservices.repositories.VehiculeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("Vehicule Service - Business Logic Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VehiculeServiceImplTest {

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehiculePublisher vehiculePublisher;

    @Mock
    private VehiculeMapper vehiculeMapper;

    @InjectMocks
    private VehiculeServiceImpl vehiculeService;

    private UUID garageId;
    private UUID vehiculeId;
    private VehiculeDTO validDTO;
    private Vehicule vehicule;
    private Garage garage;

    @BeforeEach
    void setUp() {
        garageId = UUID.randomUUID();
        vehiculeId = UUID.randomUUID();

        validDTO = new VehiculeDTO();
        validDTO.setBrand("Toyota");
        validDTO.setAnneeFabrication(2022);
        validDTO.setTypeCarburant("Diesel");

        vehicule = new Vehicule();
        vehicule.setId(vehiculeId);

        garage = new Garage();
        garage.setId(garageId);
    }

    // =========================
    // addVehiculeToGarage
    // =========================

    @Test
    @DisplayName("Should add a vehicle to a garage successfully when all conditions are met")
    void addVehicule_success() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(10L);
        when(vehiculeMapper.toEntity(any())).thenReturn(vehicule);
        when(vehiculeRepository.save(any())).thenReturn(vehicule);
        when(vehiculeMapper.toDto(any())).thenReturn(validDTO);

        VehiculeDTO result = vehiculeService.addVehiculeToGarage(garageId, validDTO);

        assertNotNull(result);
       // verify(vehiculePublisher).publishVehiculeCreation(vehicule);
    }

    @Test
    @DisplayName("Should throw exception when garage does not exist")
    void addVehicule_garage_not_found() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());

        assertThrows(GarageBusinessException.class,
                () -> vehiculeService.addVehiculeToGarage(garageId, validDTO));
    }

    @Test
    @DisplayName("Should throw exception when garage capacity is exceeded")
    void addVehicule_capacity_exceeded() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(50L);

        assertThrows(GarageBusinessException.class,
                () -> vehiculeService.addVehiculeToGarage(garageId, validDTO));
    }

    @Test
    @DisplayName("Should reject vehicle with invalid brand")
    void addVehicule_invalid_brand() {
        validDTO.setBrand("");

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(10L);

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.addVehiculeToGarage(garageId, validDTO));
    }

    @Test
    @DisplayName("Should reject vehicle with invalid manufacturing year")
    void addVehicule_invalid_year() {
        validDTO.setAnneeFabrication(0);

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(10L);

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.addVehiculeToGarage(garageId, validDTO));
    }

    @Test
    @DisplayName("Should reject vehicle with missing fuel type")
    void addVehicule_invalid_fuel() {
        validDTO.setTypeCarburant("");

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(10L);

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.addVehiculeToGarage(garageId, validDTO));
    }

    // =========================
    // updateVehicule
    // =========================

    @Test
    @DisplayName("Should update an existing vehicle successfully")
    void updateVehicule_success() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(vehiculeMapper.toDto(any())).thenReturn(validDTO);

        VehiculeDTO result = vehiculeService.updateVehicule(vehiculeId, validDTO);

        assertNotNull(result);
        verify(vehiculeMapper).updateEntityFromDto(validDTO, vehicule);
        verify(vehiculeRepository).save(vehicule);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existing vehicle")
    void updateVehicule_not_found() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        assertThrows(VehiculeNotFoundException.class,
                () -> vehiculeService.updateVehicule(vehiculeId, validDTO));
    }

    @Test
    @DisplayName("Should reject update when vehicle data is invalid")
    void updateVehicule_invalid_data() {
        validDTO.setBrand(null);

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.updateVehicule(vehiculeId, validDTO));
    }

    // =========================
    // deleteVehicule
    // =========================

    @Test
    @DisplayName("Should delete a vehicle successfully")
    void deleteVehicule_success() {
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(true);

        vehiculeService.deleteVehicule(vehiculeId);

        verify(vehiculeRepository).deleteById(vehiculeId);
    }

    @Test
    @DisplayName("Should throw exception when deleting a non-existing vehicle")
    void deleteVehicule_not_found() {
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(false);

        assertThrows(VehiculeNotFoundException.class,
                () -> vehiculeService.deleteVehicule(vehiculeId));
    }

    // =========================
    // getVehiculesByGarage
    // =========================

    @Test
    @DisplayName("Should return vehicles for a given garage")
    void getVehiculesByGarage_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicule> page = new PageImpl<>(List.of(vehicule));

        when(garageRepository.existsById(garageId)).thenReturn(true);
        when(vehiculeRepository.findByGarageId(garageId, pageable)).thenReturn(page);
        when(vehiculeMapper.toDto(any())).thenReturn(validDTO);

        Page<VehiculeDTO> result = vehiculeService.getVehiculesByGarage(garageId, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should throw exception when garage does not exist for retrieval")
    void getVehiculesByGarage_not_found() {
        Pageable pageable = PageRequest.of(0, 10);

        when(garageRepository.existsById(garageId)).thenReturn(false);

        assertThrows(GarageBusinessException.class,
                () -> vehiculeService.getVehiculesByGarage(garageId, pageable));
    }

    // =========================
    // getVehiculesByBrand
    // =========================

    @Test
    @DisplayName("Should return vehicles filtered by brand")
    void getVehiculesByBrand_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicule> page = new PageImpl<>(List.of(vehicule));

        when(vehiculeRepository.findByBrandAcrossGarages("Toyota", pageable)).thenReturn(page);
        when(vehiculeMapper.toDto(any())).thenReturn(validDTO);

        Page<VehiculeDTO> result = vehiculeService.getVehiculesByBrand("Toyota", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should reject search when brand is null or empty")
    void getVehiculesByBrand_invalid() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(IllegalArgumentException.class,
                () -> vehiculeService.getVehiculesByBrand("", pageable));
    }
}