package com.example.microservices.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.time.DayOfWeek;

@Getter
@Setter
public class GarageDTO {
    private UUID id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private Map<DayOfWeek, List<OpeningTimeDTO>> horairesOuverture;

}