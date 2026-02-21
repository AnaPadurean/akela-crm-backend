package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;

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

    public Long getCoachLocationAssocId() { return coachLocationAssocId; }
    public void setCoachLocationAssocId(Long coachLocationAssocId) { this.coachLocationAssocId = coachLocationAssocId; }

    public CoachEntity getCoach() { return coach; }
    public void setCoach(CoachEntity coach) { this.coach = coach; }

    public LocationEntity getLocation() { return location; }
    public void setLocation(LocationEntity location) { this.location = location; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
