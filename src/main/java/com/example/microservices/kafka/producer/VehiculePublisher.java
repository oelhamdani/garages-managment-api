package com.example.microservices.kafka.producer;

import com.example.microservices.model.Vehicule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VehiculePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // Replace "vehicle-topic" with the actual Kafka topic name you want to use
    private static final String TOPIC = "vehicle-topic";

    public VehiculePublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {

        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishVehiculeCreation(Vehicule vehicule) {
        // Construct a simple message or convert the Vehicule object to JSON
        try{
            String message = objectMapper.writeValueAsString(vehicule);

            // Send the message to Kafka
            kafkaTemplate.send(TOPIC, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send message", ex);
                        } else {
                            log.info("Message sent: {}", result.getRecordMetadata());
                        }
                    });
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Error while converting vehicle to json", jsonProcessingException);
            throw new RuntimeException(jsonProcessingException);
        }

    }
}