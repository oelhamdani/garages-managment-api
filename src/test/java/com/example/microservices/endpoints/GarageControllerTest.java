package com.example.microservices.endpoints;

import com.example.microservices.dtos.GarageDTO;
import com.example.microservices.services.GarageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GarageController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Garage Controller - REST API Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GarageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GarageService garageService;

    @Autowired
    private ObjectMapper objectMapper;

    private GarageDTO garageDTO;
    private UUID garageId;

    @BeforeEach
    void setUp() {
        garageId = UUID.randomUUID();

        garageDTO = new GarageDTO();
        garageDTO.setId(garageId);
        garageDTO.setName("My Garage");
        garageDTO.setAddress("Casablanca");
    }

    // =========================
    // CREATE
    // =========================

    @Test
    @DisplayName("Should create a garage and return 201 CREATED")
    void createGarage_success() throws Exception {
        Mockito.when(garageService.createGarage(any())).thenReturn(garageDTO);

        mockMvc.perform(post("/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("My Garage"));
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    @DisplayName("Should update a garage and return 200 OK")
    void updateGarage_success() throws Exception {
        Mockito.when(garageService.updateGarage(eq(garageId), any())).thenReturn(garageDTO);

        mockMvc.perform(put("/garages/{id}", garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Garage"));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    @DisplayName("Should delete a garage and return 204 NO CONTENT")
    void deleteGarage_success() throws Exception {
        Mockito.doNothing().when(garageService).deleteGarage(garageId);

        mockMvc.perform(delete("/garages/{id}", garageId))
                .andExpect(status().isNoContent());
    }

    // =========================
    // GET BY ID
    // =========================

    @Test
    @DisplayName("Should return a garage by id")
    void getGarage_success() throws Exception {
        Mockito.when(garageService.getGarageById(garageId)).thenReturn(garageDTO);

        mockMvc.perform(get("/garages/{id}", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId.toString()));
    }

    // =========================
    // GET ALL (PAGINATION)
    // =========================

    @Test
    @DisplayName("Should return paginated garages")
    void getAllGarages_success() throws Exception {
        Page<GarageDTO> page = new PageImpl<>(List.of(garageDTO));

        Mockito.when(garageService.getAllGarages(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/garages")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("My Garage"));
    }
}