package com.example.akela.swim.crm.graphs;// src/main/java/com/example/akela/swim/crm/dto/DailyPointDTO.java


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyPointDTO {
    private LocalDate day;
    private double total;
}
