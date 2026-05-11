package com.example.microservices.kafka.consumer;

import com.example.microservices.services.VehiculeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VehiculeConsumer {

    private final VehiculeService vehiculeService;

    public VehiculeConsumer(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @KafkaListener(topics = "vehicle-topic", groupId = "garage-group")
    public void listen(String message) {
        vehiculeService.handleVehiculeCreatedEvent(message);
    }
}