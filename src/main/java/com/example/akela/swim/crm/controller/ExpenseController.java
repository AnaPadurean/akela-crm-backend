package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.CreateExpenseDTO;
import com.example.akela.swim.crm.dto.ExpenseDTO;
import com.example.akela.swim.crm.entity.ExpenseEntity;
import com.example.akela.swim.crm.service.expenses.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private ExpenseDTO toDto(ExpenseEntity e) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setExpenseId(e.getExpenseId());
        dto.setTitle(e.getTitle());
        dto.setExpenseDate(e.getExpenseDate());
        dto.setAmount(e.getAmount());
        dto.setNotes(e.getNotes());
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAll() {
        List<ExpenseDTO> dtos = expenseService.findAll().stream().map(this::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpenseEntity>> search(@RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(expenseService.findByRange(from, to));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getById(@PathVariable Long id) {
        return expenseService.findById(id).map(e -> ResponseEntity.ok(toDto(e))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateExpenseDTO dto) {
        try {
            ExpenseEntity e = new ExpenseEntity();
            e.setTitle(dto.getTitle());
            e.setExpenseDate(dto.getExpenseDate());
            e.setAmount(dto.getAmount());
            e.setNotes(dto.getNotes());

            ExpenseEntity saved = expenseService.save(e);
            return ResponseEntity.ok(toDto(saved));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Eroare la salvare: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateExpenseDTO dto) {
        try {
            ExpenseEntity existing = expenseService.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));

            existing.setTitle(dto.getTitle());
            existing.setExpenseDate(dto.getExpenseDate());
            existing.setAmount(dto.getAmount());
            existing.setNotes(dto.getNotes());

            ExpenseEntity updated = expenseService.save(existing);
            return ResponseEntity.ok(toDto(updated));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Eroare la actualizare: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
