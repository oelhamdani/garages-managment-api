package com.example.microservices.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Accessoire {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nom;
    private String description;
    private BigDecimal prix;
    private String type;

    // ❌ REMOVE CascadeType.ALL here
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;
}