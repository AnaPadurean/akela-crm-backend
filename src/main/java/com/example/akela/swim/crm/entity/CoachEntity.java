package com.example.akela.swim.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
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


}
