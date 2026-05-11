package com.example.microservices.services;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.exception.AccessoireNotFoundException;
import com.example.microservices.exception.VehiculeNotFoundException;
import com.example.microservices.mapping.AccessoireMapper;
import com.example.microservices.model.Accessoire;
import com.example.microservices.model.Vehicule;
import com.example.microservices.repositories.AccessoireRepository;
import com.example.microservices.repositories.VehiculeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("Accessoire Service - Business Logic Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccessoireServiceImplTest {

    @Mock
    private AccessoireRepository accessoireRepository;

    @Mock
    private VehiculeRepository vehiculeRepository;

    @Mock
    private AccessoireMapper accessoireMapper;

    @InjectMocks
    private AccessoireServiceImpl accessoireService;

    private UUID vehiculeId;
    private UUID accessoireId;
    private AccessoireDTO validDTO;
    private Accessoire accessoire;
    private Vehicule vehicule;

    @BeforeEach
    void setUp() {
        vehiculeId = UUID.randomUUID();
        accessoireId = UUID.randomUUID();

        validDTO = new AccessoireDTO();
        validDTO.setNom("GPS");
        validDTO.setType("Electronics");
        validDTO.setDescription("Navigation system");
        validDTO.setPrix(BigDecimal.valueOf(100));

        accessoire = new Accessoire();
        accessoire.setId(accessoireId);

        vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
    }

    // =========================
    // addAccessoireToVehicule
    // =========================

    @Test
    @DisplayName("Should add an accessory to a vehicle successfully")
    void addAccessoire_success() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));
        when(accessoireMapper.toEntity(any())).thenReturn(accessoire);
        when(accessoireRepository.save(any())).thenReturn(accessoire);
        when(accessoireMapper.toDto(any())).thenReturn(validDTO);

        AccessoireDTO result = accessoireService.addAccessoireToVehicule(vehiculeId, validDTO);

        assertNotNull(result);
        verify(accessoireRepository).save(accessoire);
    }

    @Test
    @DisplayName("Should throw exception when vehicle is not found while adding accessory")
    void addAccessoire_vehicle_not_found() {
        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.empty());

        assertThrows(AccessoireNotFoundException.class,
                () -> accessoireService.addAccessoireToVehicule(vehiculeId, validDTO));
    }

    @Test
    @DisplayName("Should reject accessory with invalid name")
    void addAccessoire_invalid_name() {
        validDTO.setNom("");

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        assertThrows(IllegalArgumentException.class,
                () -> accessoireService.addAccessoireToVehicule(vehiculeId, validDTO));
    }

    @Test
    @DisplayName("Should reject accessory with invalid type")
    void addAccessoire_invalid_type() {
        validDTO.setType("");

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        assertThrows(IllegalArgumentException.class,
                () -> accessoireService.addAccessoireToVehicule(vehiculeId, validDTO));
    }

    @Test
    @DisplayName("Should reject accessory with missing description")
    void addAccessoire_invalid_description() {
        validDTO.setDescription("");

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        assertThrows(IllegalArgumentException.class,
                () -> accessoireService.addAccessoireToVehicule(vehiculeId, validDTO));
    }

    @Test
    @DisplayName("Should reject accessory with missing price")
    void addAccessoire_invalid_price() {
        validDTO.setPrix(null);

        when(vehiculeRepository.findById(vehiculeId)).thenReturn(Optional.of(vehicule));

        assertThrows(IllegalArgumentException.class,
                () -> accessoireService.addAccessoireToVehicule(vehiculeId, validDTO));
    }

    // =========================
    // updateAccessoire
    // =========================

    @Test
    @DisplayName("Should update an existing accessory successfully")
    void updateAccessoire_success() {
        when(accessoireRepository.findById(accessoireId)).thenReturn(Optional.of(accessoire));
        when(accessoireMapper.toDto(any())).thenReturn(validDTO);

        AccessoireDTO result = accessoireService.updateAccessoire(accessoireId, validDTO);

        assertNotNull(result);
        verify(accessoireMapper).updateEntityFromDto(validDTO, accessoire);
        verify(accessoireRepository).save(accessoire);
    }

    @Test
    @DisplayName("Should throw exception when accessory not found during update")
    void updateAccessoire_not_found() {
        when(accessoireRepository.findById(accessoireId)).thenReturn(Optional.empty());

        assertThrows(AccessoireNotFoundException.class,
                () -> accessoireService.updateAccessoire(accessoireId, validDTO));
    }

    @Test
    @DisplayName("Should reject update when accessory data is invalid")
    void updateAccessoire_invalid_data() {
        validDTO.setNom(null);

        when(accessoireRepository.findById(accessoireId)).thenReturn(Optional.of(accessoire));

        assertThrows(IllegalArgumentException.class,
                () -> accessoireService.updateAccessoire(accessoireId, validDTO));
    }

    // =========================
    // deleteAccessoire
    // =========================

    @Test
    @DisplayName("Should delete an accessory successfully")
    void deleteAccessoire_success() {
        when(accessoireRepository.findById(accessoireId)).thenReturn(Optional.of(accessoire));

        accessoireService.deleteAccessoire(accessoireId);

        verify(accessoireRepository).delete(accessoire);
    }

    @Test
    @DisplayName("Should throw exception when accessory not found during deletion")
    void deleteAccessoire_not_found() {
        when(accessoireRepository.findById(accessoireId)).thenReturn(Optional.empty());

        assertThrows(AccessoireNotFoundException.class,
                () -> accessoireService.deleteAccessoire(accessoireId));
    }

    // =========================
    // getAccessoiresByVehicule
    // =========================

    @Test
    @DisplayName("Should return list of accessories for a vehicle")
    void getAccessoiresByVehicule_success() {
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(true);
        when(accessoireRepository.findByVehiculeId(vehiculeId)).thenReturn(List.of(accessoire));
        when(accessoireMapper.toDto(any())).thenReturn(validDTO);

        List<AccessoireDTO> result = accessoireService.getAccessoiresByVehicule(vehiculeId);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should throw exception when vehicle not found for accessory retrieval")
    void getAccessoiresByVehicule_vehicle_not_found() {
        when(vehiculeRepository.existsById(vehiculeId)).thenReturn(false);

        assertThrows(VehiculeNotFoundException.class,
                () -> accessoireService.getAccessoiresByVehicule(vehiculeId));
    }
}