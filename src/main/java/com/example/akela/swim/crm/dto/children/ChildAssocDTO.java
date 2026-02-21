package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildAssocDTO {
    Long parentChildId;
    Long childId;
    String relationshipType;
    boolean legalGuardian;
    String childFirstName;
    String childLastName;

    public ChildAssocDTO(Long parentChildId, Long childId, String relationshipType, boolean legalGuardian,
                         String childFirstName, String childLastName) {
        this.parentChildId = parentChildId;
        this.childId = childId;
        this.relationshipType = relationshipType;
        this.legalGuardian = legalGuardian;
        this.childFirstName = childFirstName;
        this.childLastName = childLastName;
    }

}
