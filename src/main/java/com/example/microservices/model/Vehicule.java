package com.example.microservices.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String brand;
    private int anneeFabrication;
    private String typeCarburant;

    // ❌ REMOVE CascadeType.ALL here
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private Garage garage;

    // ✅ Bidirectional
    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accessoire> accessoires = new ArrayList<>();
}