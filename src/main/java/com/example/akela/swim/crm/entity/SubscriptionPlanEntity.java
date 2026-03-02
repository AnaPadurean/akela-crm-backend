package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "akl_subscription_plans")
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_plan_id")
    private Long subscriptionPlanId;
    private Integer sessions;
    private Double price;
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_type_id", nullable = false)
    private SubscriptionTypeEntity subscriptionType;


}
