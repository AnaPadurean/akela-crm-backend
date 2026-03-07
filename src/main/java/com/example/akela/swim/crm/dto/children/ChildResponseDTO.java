package com.example.akela.swim.crm.dto.children;

import lombok.Data;

import java.util.Date;

@Data
public class ChildResponseDTO {
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private Date birthday;
    private String childUniqueIdentifier;
    private boolean status;
}
