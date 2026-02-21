package com.example.akela.swim.crm.dto;

import java.util.List;

public class CreateUpdateCoachDTO {

    private String coachFirstName;
    private String coachLastName;
    private String phone;
    private String email;
    private String address;

    private List<Long> locationIds;

    public String getCoachFirstName() { return coachFirstName; }
    public void setCoachFirstName(String coachFirstName) { this.coachFirstName = coachFirstName; }

    public String getCoachLastName() { return coachLastName; }
    public void setCoachLastName(String coachLastName) { this.coachLastName = coachLastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Long> getLocationIds() { return locationIds; }
    public void setLocationIds(List<Long> locationIds) { this.locationIds = locationIds; }
}
