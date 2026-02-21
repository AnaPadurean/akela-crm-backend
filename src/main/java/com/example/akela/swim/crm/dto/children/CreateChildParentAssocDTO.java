package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class CreateChildParentAssocDTO {
    private Long parentId;
    private Long childId;
    private String relationshipType;
    private boolean legalGuardian;
}
