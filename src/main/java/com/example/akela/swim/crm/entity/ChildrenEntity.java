package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "akl_children")
public class ChildrenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long childId;

    private String childFirstName;
    private String childLastName;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private String childUniqueIdentifier;

    @Transient
    private String rawBirthday;

    private boolean status = true;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildParentAssocEntity> parentChildren = new HashSet<>();

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildCoachAssocEntity> coachChildren = new HashSet<>();

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getChildFirstName() {
        return childFirstName;
    }

    public void setChildFirstName(String childFirstName) {
        this.childFirstName = childFirstName;
    }

    public String getChildLastName() {
        return childLastName;
    }

    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getChildUniqueIdentifier() {
        return childUniqueIdentifier;
    }

    public void setChildUniqueIdentifier(String childUniqueIdentifier) {
        this.childUniqueIdentifier = childUniqueIdentifier;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Set<ChildParentAssocEntity> getParentChildren() {
        return parentChildren;
    }

    public void setParentChildren(Set<ChildParentAssocEntity> parentChildren) {
        this.parentChildren = parentChildren;
    }

    public Set<ChildCoachAssocEntity> getCoachChildren() {
        return coachChildren;
    }

    public void setCoachChildren(Set<ChildCoachAssocEntity> coachChildren) {
        this.coachChildren = coachChildren;
    }

    // ========================= LOGICĂ SUPLIMENTARĂ =========================

    public String getRawBirthday() {
        return rawBirthday;
    }

    /**
     * Primește un string (ex: "2020" sau "12/05/2020")
     * și îl transformă automat în Date validă.
     */
    public void setRawBirthday(String rawBirthday) {
        this.rawBirthday = rawBirthday;

        if (rawBirthday == null || rawBirthday.trim().isEmpty()) {
            this.birthday = null;
            return;
        }

        this.birthday = parseFlexibleDate(rawBirthday.trim());
    }

    private Date parseFlexibleDate(String value) {
        try {
            if (value.matches("^\\d{4}$")) {
                return new SimpleDateFormat("yyyy-MM-dd").parse(value + "-01-01");
            }
            else if (value.matches("^\\d{1,2}[./-]\\d{1,2}[./-]\\d{4}$")) {
                value = value.replace(".", "/").replace("-", "/");
                return new SimpleDateFormat("dd/MM/yyyy").parse(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
