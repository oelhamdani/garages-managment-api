package com.example.microservices.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class OpeningTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}