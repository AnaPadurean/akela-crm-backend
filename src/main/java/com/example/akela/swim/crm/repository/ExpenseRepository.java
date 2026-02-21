package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByExpenseDateBetweenOrderByExpenseDateDesc(LocalDate from, LocalDate to);

}
