package com.example.akela.swim.crm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WeeklyTrainingSessionDTO {
    private Long id;
    private List<Long> childIds;
    private Long coachId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String notes;
}
