package com.example.akela.swim.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "akl_subscription_payments")
public class SubscriptionPaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionEntity subscription;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "paid_date", nullable = false)
    private LocalDate paidDate;

    @Column(name = "observations")
    private String observations;
}

