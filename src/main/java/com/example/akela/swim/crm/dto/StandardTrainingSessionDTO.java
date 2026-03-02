package com.example.akela.swim.crm.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class StandardTrainingSessionDTO {

    private Long id;
    private List<Long> childIds;
    private Long coachId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;

}