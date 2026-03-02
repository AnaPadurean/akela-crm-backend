package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildDTO {
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private String rawBirthday;
    private String childUniqueIdentifier;
    private Boolean status;

}
