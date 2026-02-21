package com.example.akela.swim.crm.dto.parents;

import lombok.Data;

@Data
public class ParentAssocDTO {
    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String email;
    private String relationshipType;
    private boolean legalGuardian;
    private String address;
}
