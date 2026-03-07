package com.example.akela.swim.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
@Entity
@Table(name = "akl_assoc_coach_children")
public class ChildCoachAssocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assoc_coach_child_id")
    private Long coachChildId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = false)
    @JsonIgnore
    private ChildrenEntity child;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnore
    private CoachEntity coach;

    private String trainingType;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChildCoachAssocEntity that = (ChildCoachAssocEntity) o;
        return Objects.equals(coachChildId, that.coachChildId) && Objects.equals(child, that.child) && Objects.equals(coach, that.coach) && Objects.equals(trainingType, that.trainingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coachChildId, child, coach, trainingType);
    }
}
