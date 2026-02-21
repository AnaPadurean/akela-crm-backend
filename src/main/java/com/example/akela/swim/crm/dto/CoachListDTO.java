package com.example.akela.swim.crm.dto;

import java.util.List;

public class CoachListDTO {

    private Long coachId;
    private String coachFirstName;
    private String coachLastName;
    private String phone;
    private String email;
    private String address;

    private List<Long> locationIds;

    public CoachListDTO(Long coachId, String coachFirstName, String coachLastName,
                        String phone, String email, String address, List<Long> locationIds) {
        this.coachId = coachId;
        this.coachFirstName = coachFirstName;
        this.coachLastName = coachLastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.locationIds = locationIds;
    }

    public Long getCoachId() { return coachId; }
    public String getCoachFirstName() { return coachFirstName; }
    public String getCoachLastName() { return coachLastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public List<Long> getLocationIds() { return locationIds; }
}
