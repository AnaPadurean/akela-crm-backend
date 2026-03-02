package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "akl_dd_subscription_type")
public class SubscriptionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_type_id")
    private Long subscriptionTypeId;

    private String code;
    private String name;
    private Integer maxChildren;
    private String description;

    @OneToMany(mappedBy = "subscriptionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubscriptionPlanEntity> plans = new HashSet<>();

}
