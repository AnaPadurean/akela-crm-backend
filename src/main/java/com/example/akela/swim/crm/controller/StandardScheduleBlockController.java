package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.StandardScheduleBlockBulkDTO;
import com.example.akela.swim.crm.entity.StandardScheduleBlockEntity;
import com.example.akela.swim.crm.service.StandardScheduleBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standard-blocks")

public class StandardScheduleBlockController {

    private final StandardScheduleBlockService service;

    public StandardScheduleBlockController(StandardScheduleBlockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StandardScheduleBlockEntity>> getByCoach(@RequestParam Long coachId) {
        return ResponseEntity.ok(service.findByCoach(coachId));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<StandardScheduleBlockEntity>> bulk(@RequestBody StandardScheduleBlockBulkDTO dto) {
        return ResponseEntity.ok(service.bulkCreate(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
