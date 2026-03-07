package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildParentAssocResponseDTO {
    private Long parentChildId;
    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private String relationshipType;
    private boolean legalGuardian;
}
