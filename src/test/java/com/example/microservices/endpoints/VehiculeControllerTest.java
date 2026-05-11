package com.example.microservices.endpoints;

import com.example.microservices.dtos.VehiculeDTO;
import com.example.microservices.services.VehiculeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehiculeController.class)
@DisplayName("Vehicle Controller - REST API Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehiculeService vehiculeService;

    @Autowired
    private ObjectMapper objectMapper;

    private VehiculeDTO vehiculeDTO;
    private UUID garageId;
    private UUID vehiculeId;

    @BeforeEach
    void setUp() {
        garageId = UUID.randomUUID();
        vehiculeId = UUID.randomUUID();

        vehiculeDTO = new VehiculeDTO();
        vehiculeDTO.setId(vehiculeId);
        vehiculeDTO.setBrand("Toyota");
        vehiculeDTO.setAnneeFabrication(2020);
        vehiculeDTO.setTypeCarburant("Diesel");
        vehiculeDTO.setGarageId(garageId);
    }

    // =========================
    // ADD VEHICLE TO GARAGE
    // =========================
    @Test
    void should_add_vehicle_to_garage() throws Exception {

        Mockito.when(vehiculeService.addVehiculeToGarage(eq(garageId), any()))
                .thenReturn(vehiculeDTO);

        mockMvc.perform(post("/garages/{garageId}/vehicle", garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    // =========================
    // UPDATE VEHICLE
    // =========================
    @Test
    void should_update_vehicle() throws Exception {

        Mockito.when(vehiculeService.updateVehicule(eq(vehiculeId), any()))
                .thenReturn(vehiculeDTO);

        mockMvc.perform(put("/vehicles/{id}", vehiculeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));
    }

    // =========================
    // DELETE VEHICLE
    // =========================
    @Test
    void should_delete_vehicle() throws Exception {

        Mockito.doNothing().when(vehiculeService).deleteVehicule(vehiculeId);

        mockMvc.perform(delete("/vehicles/{id}", vehiculeId))
                .andExpect(status().isNoContent());
    }

    // =========================
    // GET BY GARAGE
    // =========================
    @Test
    void should_get_vehicles_by_garage() throws Exception {

        Page<VehiculeDTO> page = new PageImpl<>(List.of(vehiculeDTO));

        Mockito.when(vehiculeService.getVehiculesByGarage(eq(garageId), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/garages/{garageId}/vehicles", garageId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].brand").value("Toyota"));
    }

    // =========================
    // GET BY BRAND
    // =========================
    @Test
    void should_get_vehicles_by_brand() throws Exception {

        Page<VehiculeDTO> page = new PageImpl<>(List.of(vehiculeDTO));

        Mockito.when(vehiculeService.getVehiculesByBrand(eq("Toyota"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/vehicles")
                        .param("brand", "Toyota")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].brand").value("Toyota"));
    }
}