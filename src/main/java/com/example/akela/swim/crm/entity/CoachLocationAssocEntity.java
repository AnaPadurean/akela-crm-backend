package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(
        name = "akl_assoc_coach_location",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"coach_id", "location_id"})
        }
)
public class CoachLocationAssocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coach_location_assoc_id")
    private Long coachLocationAssocId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private CoachEntity coach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(nullable = false)
    private Boolean active = true;

}
