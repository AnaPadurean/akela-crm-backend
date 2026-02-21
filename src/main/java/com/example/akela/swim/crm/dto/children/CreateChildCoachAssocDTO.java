package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class CreateChildCoachAssocDTO {
    private Long coachId;
    private Long childId;
    private String trainingType;
}
