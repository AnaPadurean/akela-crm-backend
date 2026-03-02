package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SessionDTO {

    private Long sessionId;
    private LocalDateTime scheduledDate;
    private Boolean completed;
    private Boolean canceled;

    private Long subscriptionId;
    private String subscriptionStatus;
    private Integer totalSessions;
    private Integer completedSessions;
    private Integer remainingSessions;

    private String childFullName;
    private String coachFullName;
    private Long coachId;


}
