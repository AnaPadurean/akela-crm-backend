package com.example.akela.swim.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "akl_coaches")
public class CoachEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coach_id")
    private Long coachId;

    private String coachFirstName;
    private String coachLastName;
    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildCoachAssocEntity> coachChildren = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CoachLocationAssocEntity> coachLocations = new HashSet<>();


    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public String getCoachLastName() {
        return coachLastName;
    }

    public void setCoachLastName(String coachLastName) {
        this.coachLastName = coachLastName;
    }

    public String getCoachFirstName() {
        return coachFirstName;
    }

    public void setCoachFirstName(String coachFirstName) {
        this.coachFirstName = coachFirstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<ChildCoachAssocEntity> getCoachChildren() {
        return coachChildren;
    }

    public void setCoachChildren(Set<ChildCoachAssocEntity> coachChildren) {
        this.coachChildren = coachChildren;
    }

    public Set<CoachLocationAssocEntity> getCoachLocations() { return coachLocations; }
    public void setCoachLocations(Set<CoachLocationAssocEntity> coachLocations) { this.coachLocations = coachLocations; }
}
