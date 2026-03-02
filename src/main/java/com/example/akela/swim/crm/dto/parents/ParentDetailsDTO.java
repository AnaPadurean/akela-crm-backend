package com.example.akela.swim.crm.dto.parents;

import lombok.Data;

@Data
public class ParentDetailsDTO {
    private Long parentId;
    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String address;
    private String email;
    private String relationshipType;
    private boolean legalGuardian;

    public ParentDetailsDTO(Long parentId, String parentFirstName, String parentLastName, String phone, String email, String relationshipType, boolean legalGuardian, String address) {
        this.parentId = parentId;
        this.parentFirstName = parentFirstName;
        this.parentLastName = parentLastName;
        this.phone = phone;
        this.email = email;
        this.relationshipType = relationshipType;
        this.legalGuardian = legalGuardian;
        this.address = address;
    }
}
