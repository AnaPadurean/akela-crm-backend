package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class ExpenseDTO {
    private Long expenseId;
    private String title;
    private LocalDate expenseDate;
    private BigDecimal amount;
    private String notes;

}
