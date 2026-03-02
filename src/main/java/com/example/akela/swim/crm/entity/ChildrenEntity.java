package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "akl_children")
public class ChildrenEntity {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long childId;

    @Setter
    private String childFirstName;
    @Setter
    private String childLastName;

    @Setter
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Setter
    private String childUniqueIdentifier;

    @Transient
    private String rawBirthday;

    @Setter
    private boolean status = true;

    @Setter
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildParentAssocEntity> parentChildren = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChildCoachAssocEntity> coachChildren = new HashSet<>();


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
            } else if (value.matches("^\\d{1,2}[./-]\\d{1,2}[./-]\\d{4}$")) {
                value = value.replace(".", "/").replace("-", "/");
                return new SimpleDateFormat("dd/MM/yyyy").parse(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
