package com.example.akela.swim.crm.dto.subscription;

import lombok.Data;

@Data
public class SubscriptionDTO {
    private Long subscriptionId;
    private String childFullName;
    private String coachFullName;
    private String planName;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private Integer totalSessions;
    private Integer completedSessions;
    private Integer remainingSessions;
    private String status;
    private Boolean isPaid;
    private Long paymentId;


}
