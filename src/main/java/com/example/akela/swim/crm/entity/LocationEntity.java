package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "akl_locations")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(nullable = false)
    private String name;

    @Column(name = "name_norm", nullable = false)
    private String nameNorm;

    private String address;

    @Column(nullable = false)
    private Boolean active = true;

}
