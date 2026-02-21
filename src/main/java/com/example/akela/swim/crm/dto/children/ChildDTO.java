package com.example.akela.swim.crm.dto.children;

import lombok.Data;

@Data
public class ChildDTO {
    private Long childId; // null la ADD
    private String childFirstName;
    private String childLastName;
    private String rawBirthday;
    private String childUniqueIdentifier;
    private Boolean status;

}
