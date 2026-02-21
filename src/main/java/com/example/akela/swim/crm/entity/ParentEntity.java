package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "akl_parents")
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long parentId;

    private String parentFirstName;
    private String parentLastName;
    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true
    )
    private Set<ChildParentAssocEntity> parentChildren = new HashSet<>();

    public Set<ChildParentAssocEntity> getParentChildren() {
        return parentChildren;
    }

    public void setParentChildren(Set<ChildParentAssocEntity> parentChildren) {
        this.parentChildren = parentChildren;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentLastName() {
        return parentLastName;
    }

    public void setParentLastName(String parentLastName) {
        this.parentLastName = parentLastName;
    }

    public String getParentFirstName() {
        return parentFirstName;
    }

    public void setParentFirstName(String parentFirstName) {
        this.parentFirstName = parentFirstName;
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
}
