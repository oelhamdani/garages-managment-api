package com.example.microservices.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class OpeningTime {

    private LocalTime startTime;
    private LocalTime endTime;

    // Getters, Setters, Constructeurs
}