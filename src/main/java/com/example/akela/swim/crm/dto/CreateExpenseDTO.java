package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
public class CreateExpenseDTO {
    private String title;
    private LocalDate expenseDate;
    private BigDecimal amount;
    private String notes;

}
