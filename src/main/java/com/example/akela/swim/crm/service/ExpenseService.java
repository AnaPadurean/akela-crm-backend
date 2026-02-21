package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.entity.ExpenseEntity;
import com.example.akela.swim.crm.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<ExpenseEntity> findAll() {
        return repo.findAll();
    }

    public Optional<ExpenseEntity> findById(Long id) {
        return repo.findById(id);
    }

    public ExpenseEntity save(ExpenseEntity e) {
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<ExpenseEntity> findByRange(String from, String to) {
        LocalDate start = LocalDate.parse(from); // "YYYY-MM-DD"
        LocalDate end = LocalDate.parse(to);
        return repo.findByExpenseDateBetweenOrderByExpenseDateDesc(start, end);
    }

}
