package com.example.akela.swim.crm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Could not convert list to JSON", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Could not convert JSON to list", e);
        }
    }
}
