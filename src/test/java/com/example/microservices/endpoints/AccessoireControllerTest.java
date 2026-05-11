package com.example.microservices.endpoints;

import com.example.microservices.dtos.AccessoireDTO;
import com.example.microservices.services.AccessoireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessoireController.class)
@DisplayName("Accessoire Controller - REST API Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccessoireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessoireService accessoireService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccessoireDTO accessoireDTO;
    private UUID vehiculeId;
    private UUID accessoireId;

    @BeforeEach
    void setUp() {
        vehiculeId = UUID.randomUUID();
        accessoireId = UUID.randomUUID();

        accessoireDTO = new AccessoireDTO();
        accessoireDTO.setId(accessoireId);
        accessoireDTO.setNom("GPS");
        accessoireDTO.setType("Navigation");
        accessoireDTO.setDescription("Car GPS system");
        accessoireDTO.setPrix(BigDecimal.valueOf(100));
    }

    // =========================
    // ADD ACCESSORY
    // =========================
    @Test
    void should_add_accessoire_to_vehicule() throws Exception {

        Mockito.when(accessoireService.addAccessoireToVehicule(eq(vehiculeId), any()))
                .thenReturn(accessoireDTO);

        mockMvc.perform(post("/vehicules/{vehiculeId}/accessoire", vehiculeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessoireDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("GPS"));
    }

    // =========================
    // UPDATE ACCESSORY
    // =========================
    @Test
    void should_update_accessoire() throws Exception {

        Mockito.when(accessoireService.updateAccessoire(eq(accessoireId), any()))
                .thenReturn(accessoireDTO);

        mockMvc.perform(put("/accessoires/{id}", accessoireId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessoireDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("GPS"));
    }

    // =========================
    // DELETE ACCESSORY
    // =========================
    @Test
    void should_delete_accessoire() throws Exception {

        Mockito.doNothing().when(accessoireService).deleteAccessoire(accessoireId);

        mockMvc.perform(delete("/accessoires/{id}", accessoireId))
                .andExpect(status().isNoContent());
    }

    // =========================
    // GET BY VEHICULE
    // =========================
    @Test
    void should_get_accessoires_by_vehicule() throws Exception {

        Mockito.when(accessoireService.getAccessoiresByVehicule(vehiculeId))
                .thenReturn(List.of(accessoireDTO));

        mockMvc.perform(get("/vehicules/{vehiculeId}/accessoire", vehiculeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("GPS"));
    }
}