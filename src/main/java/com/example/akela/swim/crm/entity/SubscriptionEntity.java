package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "akl_subscriptions")
public class SubscriptionEntity {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildrenEntity child;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id")
    private CoachEntity coach;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity plan;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;


    @Setter
    private Integer totalSessions;
    @Setter
    private Integer completedSessions;
    @Setter
    private Integer remainingSessions;
    @Setter
    private String status;


    @Setter
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SessionEntity> sessions = new HashSet<>();


    public void setIsPaid(Boolean isPaid) {
        this.isPaid = (isPaid != null) ? isPaid : false;
    }
}
