package com.example.akela.swim.crm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Long subscriptionId;

    private String childFullName;
    private String planName;

    private Double amount;
    private LocalDate paidDate;
    private String observations;
}
