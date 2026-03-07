package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildCoachAssocResponseDTO {
    private Long coachChildId;
    private Long coachId;
    private String coachFirstName;
    private String coachLastName;
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private String trainingType;
}
