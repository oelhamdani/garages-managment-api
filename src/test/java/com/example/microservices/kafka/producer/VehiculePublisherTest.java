package com.example.microservices.kafka.producer;

import com.example.microservices.model.Vehicule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class VehiculePublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private VehiculePublisher vehiculePublisher;

    // ✅ SUCCESS CASE
    @Test
    void shouldSendVehiculeAsJsonToKafka() throws Exception {
        Vehicule vehicule = new Vehicule();
        vehicule.setId(UUID.randomUUID());
        vehicule.setBrand("BMW");

        String json = "{\"id\":\"123\",\"brand\":\"BMW\"}";

        when(objectMapper.writeValueAsString(vehicule)).thenReturn(json);
        when(kafkaTemplate.send(anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        vehiculePublisher.publishVehiculeCreation(vehicule);

        verify(objectMapper).writeValueAsString(vehicule);
        verify(kafkaTemplate).send("vehicle-topic", json);
    }

    // ❌ JSON ERROR CASE
    @Test
    void shouldThrowExceptionWhenJsonFails() throws Exception {
        Vehicule vehicule = new Vehicule();

        when(objectMapper.writeValueAsString(vehicule))
                .thenThrow(new JsonProcessingException("error") {});

        assertThrows(RuntimeException.class, () ->
                vehiculePublisher.publishVehiculeCreation(vehicule)
        );

        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}