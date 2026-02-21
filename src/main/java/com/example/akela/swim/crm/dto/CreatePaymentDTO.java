package com.example.akela.swim.crm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePaymentDTO {
    private Double amount;
    private LocalDate paidDate;
    private String observations;
}

