package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildCoachAssocDTO {
    Long coachChildId;
    Long childId;
    String trainingType;
    String childFirstName;
    String childLastName;

    public ChildCoachAssocDTO(Long coachChildId, Long childId, String trainingType,
                              String childFirstName, String childLastName) {
        this.coachChildId = coachChildId;
        this.childId = childId;
        this.trainingType = trainingType;
        this.childFirstName = childFirstName;
        this.childLastName = childLastName;
    }
}
