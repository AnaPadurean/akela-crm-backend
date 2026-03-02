package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReservationDTO {
    private Long reservationId;
    private Long coachId;
    private String coachFullName;
    private Long sessionId;
    private Long childId;
    private String childFullName;
    private LocalDateTime reservationDateTime;
    private String status;
    private Long subscriptionId;
    private String subscriptionName;
    private Long weeklyTrainingSessionId;


}
