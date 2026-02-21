package com.example.akela.swim.crm.dto;

import lombok.Data;

@Data
public class CreateSubscriptionDTO {
    private Long childId;
    private Long coachId;
    private Long planId;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private String status;
    private Boolean isPaid;

}