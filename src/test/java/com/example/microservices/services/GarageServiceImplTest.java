package com.example.microservices.services;

import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.exception.GarageBusinessException;
import com.example.microservices.mapping.GarageMapper;
import com.example.microservices.model.Garage;
import com.example.microservices.repositories.AccessoireRepository;
import com.example.microservices.repositories.GarageRepository;
import com.example.microservices.repositories.VehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private AccessoireRepository accessoireRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private Garage garage;
    private GarageDTO garageDTO;
    private UUID garageId;

    @BeforeEach
    void setup() {
        garageId = UUID.randomUUID();

        garage = new Garage();
        garage.setId(garageId);
        garage.setName("Test Garage");

        garageDTO = new GarageDTO();
        garageDTO.setName("Test Garage");
        garageDTO.setAddress("Address");
        garageDTO.setTelephone("123456");
        garageDTO.setEmail("test@mail.com");
    }

    // ✅ CREATE
    @Test
    void createGarage_success() {
        when(garageMapper.toEntity(garageDTO)).thenReturn(garage);
        when(garageRepository.save(garage)).thenReturn(garage);
        when(garageMapper.toDto(garage)).thenReturn(garageDTO);

        GarageDTO result = garageService.createGarage(garageDTO);

        assertNotNull(result);
        verify(garageRepository).save(garage);
    }

    @Test
    void createGarage_validationFails() {
        garageDTO.setName(null);

        assertThrows(IllegalArgumentException.class,
                () -> garageService.createGarage(garageDTO));
    }

    // ✅ UPDATE
    @Test
    void updateGarage_success() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(garageRepository.save(garage)).thenReturn(garage);
        when(garageMapper.toDto(garage)).thenReturn(garageDTO);

        GarageDTO result = garageService.updateGarage(garageId, garageDTO);

        assertNotNull(result);
        verify(garageMapper).updateEntityFromDto(garageDTO, garage);
        verify(garageRepository).save(garage);
    }

    @Test
    void updateGarage_notFound() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());

        assertThrows(GarageBusinessException.class,
                () -> garageService.updateGarage(garageId, garageDTO));
    }

    // ✅ DELETE
    @Test
    void deleteGarage_success() {
        when(garageRepository.existsById(garageId)).thenReturn(true);
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(0L);

        garageService.deleteGarage(garageId);

        verify(garageRepository).deleteById(garageId);
    }

    @Test
    void deleteGarage_notFound() {
        when(garageRepository.existsById(garageId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> garageService.deleteGarage(garageId));
    }

    @Test
    void deleteGarage_hasVehicles() {
        when(garageRepository.existsById(garageId)).thenReturn(true);
        when(vehiculeRepository.countByGarageId(garageId)).thenReturn(5L);

        assertThrows(IllegalStateException.class,
                () -> garageService.deleteGarage(garageId));
    }

    // ✅ GET BY ID
    @Test
    void getGarageById_success() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(garageMapper.toDto(garage)).thenReturn(garageDTO);

        GarageDTO result = garageService.getGarageById(garageId);

        assertNotNull(result);
    }

    @Test
    void getGarageById_notFound() {
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());

        assertThrows(GarageBusinessException.class,
                () -> garageService.getGarageById(garageId));
    }

    // ✅ GET ALL
    @Test
    void getAllGarages_success() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Garage> page = new PageImpl<>(java.util.List.of(garage));

        when(garageRepository.findAll(pageable)).thenReturn(page);
        when(garageMapper.toDto(garage)).thenReturn(garageDTO);

        Page<GarageDTO> result = garageService.getAllGarages(pageable);

        assertEquals(1, result.getContent().size());
    }

    // ✅ SEARCH
    @Test
    void searchGarages_success() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Garage> page = new PageImpl<>(java.util.List.of(garage));

        when(garageRepository.searchGarages("SUV", "GPS", pageable)).thenReturn(page);
        when(garageMapper.toDto(garage)).thenReturn(garageDTO);

        Page<GarageDTO> result =
                garageService.searchGarages("SUV", "GPS", pageable);

        assertEquals(1, result.getContent().size());
    }
}