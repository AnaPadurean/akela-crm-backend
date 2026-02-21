package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.StandardTrainingSessionDTO;
import com.example.akela.swim.crm.entity.StandardTrainingSessionEntity;
import com.example.akela.swim.crm.service.StandardTrainingSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standard-sessions")
public class StandardTrainingSessionController {

    private final StandardTrainingSessionService service;

    public StandardTrainingSessionController(StandardTrainingSessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StandardTrainingSessionEntity> create(@RequestBody StandardTrainingSessionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardTrainingSessionEntity> update(
            @PathVariable Long id,
            @RequestBody StandardTrainingSessionDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StandardTrainingSessionEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }


}
