package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateUpdateCoachDTO {

    private String coachFirstName;
    private String coachLastName;
    private String phone;
    private String email;
    private String address;

    private List<Long> locationIds;

}
