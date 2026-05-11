package com.example.microservices.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Setter
public class AccessoireDTO {
    private UUID id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private String type;
    private UUID vehiculeId;

}