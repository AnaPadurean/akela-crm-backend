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
@Table(name = "akl_assoc_parent_children")
public class ChildParentAssocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assoc_parent_child_id")
    private Long parentChildId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", nullable = false)
    @JsonIgnore
    private ParentEntity parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = false)
    @JsonIgnore
    private ChildrenEntity child;

    private String relationshipType;

    private boolean isLegalGuardian;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChildParentAssocEntity that = (ChildParentAssocEntity) o;
        return Objects.equals(parentChildId, that.parentChildId) && Objects.equals(parent, that.parent) && Objects.equals(child, that.child) && Objects.equals(relationshipType, that.relationshipType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentChildId, parent, child, relationshipType);
    }
}
