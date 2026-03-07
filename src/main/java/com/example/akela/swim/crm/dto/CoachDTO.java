package com.example.akela.swim.crm.dto;

import lombok.Data;

@Data
public class CoachDTO {
    private Long coachId;
    private String coachFirstName;
    private String coachLastName;
    private String phone;
    private String email;
    private String address;
}
