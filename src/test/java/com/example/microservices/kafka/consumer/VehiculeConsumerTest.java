package com.example.microservices.kafka.consumer;

import com.example.microservices.services.VehiculeService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class VehiculeConsumerTest {

    @Test
    void shouldDelegateMessageToService() {
        // Arrange
        VehiculeService vehiculeService = mock(VehiculeService.class);
        VehiculeConsumer consumer = new VehiculeConsumer(vehiculeService);

        String message = "{\"id\":1}";

        // Act
        consumer.listen(message);

        // Assert
        verify(vehiculeService, times(1))
                .handleVehiculeCreatedEvent(message);
    }
}