package com.example.microservices.mapping;

import com.example.microservices.model.OpeningTime;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.List;

@Converter(autoApply = true)
public class GarageHorairesOuvertureConverter implements AttributeConverter<Map<DayOfWeek, List<OpeningTime>>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<DayOfWeek, List<OpeningTime>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting map to string", e);
        }
    }

    @Override
    public Map<DayOfWeek, List<OpeningTime>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<DayOfWeek, List<OpeningTime>>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting string to map", e);
        }
    }
}