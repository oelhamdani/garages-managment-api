package com.example.microservices.model;

import com.example.microservices.mapping.GarageHorairesOuvertureConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String address;
    private String telephone;
    private String email;

    @ElementCollection
    @Convert(converter = GarageHorairesOuvertureConverter.class)
    private Map<DayOfWeek, List<OpeningTime>> horairesOuverture;

    // ✅ Bidirectional relationship
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicule> vehicules = new ArrayList<>();
}





