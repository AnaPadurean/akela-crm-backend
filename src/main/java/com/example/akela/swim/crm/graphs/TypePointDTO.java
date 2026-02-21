package com.example.akela.swim.crm.graphs;// src/main/java/com/example/akela/swim/crm/dto/TypePointDTO.java


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypePointDTO {
    private String typeName;
    private double total;
    private long count;
}
