package com.example.akela.swim.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.Objects;


@Entity
@Table(name = "akl_assoc_parent_children")
public class ChildParentAssocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assoc_parent_child_id")
    private Long parentChildId;

    // updateParent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    @JsonIgnore
    private ParentEntity parent;

    // ChildrenEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    @JsonIgnore
    private ChildrenEntity child;

    private String relationshipType; // mother, father etc

    private boolean isLegalGuardian;

    public Long getParentChildId() {
        return parentChildId;
    }

    public void setParentChildId(Long parentChildId) {
        this.parentChildId = parentChildId;
    }

    public ParentEntity getParent() {
        return parent;
    }

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }

    public ChildrenEntity getChild() {
        return child;
    }

    public void setChild(ChildrenEntity child) {
        this.child = child;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public boolean isLegalGuardian() {
        return isLegalGuardian;
    }

    public void setLegalGuardian(boolean legalGuardian) {
        isLegalGuardian = legalGuardian;
    }

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
