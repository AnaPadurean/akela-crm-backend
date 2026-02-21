package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "akl_dd_subscription_type")
public class SubscriptionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_type_id")
    private Long subscriptionTypeId;

    private String code; // ex: INDIVIDUAL, MINIGRUP
    private String name;
    private Integer maxChildren;
    private String description;

    @OneToMany(mappedBy = "subscriptionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubscriptionPlanEntity> plans = new HashSet<>();

    public Long getSubscriptionTypeId() {
        return subscriptionTypeId;
    }

    public void setSubscriptionTypeId(Long subscriptionTypesId) {
        this.subscriptionTypeId = subscriptionTypesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxChildren() {
        return maxChildren;
    }

    public void setMaxChildren(Integer maxChildren) {
        this.maxChildren = maxChildren;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SubscriptionPlanEntity> getPlans() {
        return plans;
    }

    public void setPlans(Set<SubscriptionPlanEntity> plans) {
        this.plans = plans;
    }
}
