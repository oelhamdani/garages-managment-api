package com.example.microservices.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class VehiculeDTO {

    private UUID id;
    private String brand;
    private int anneeFabrication;
    private String typeCarburant;

    // optional but useful
    private UUID garageId;
    
}