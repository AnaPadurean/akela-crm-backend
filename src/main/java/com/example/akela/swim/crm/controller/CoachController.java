package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.CoachDTO;
import com.example.akela.swim.crm.dto.CoachListDTO;
import com.example.akela.swim.crm.dto.CreateUpdateCoachDTO;
import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.mapper.CoachMapper;
import com.example.akela.swim.crm.service.coach.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")

public class CoachController {

    private final CoachService coachService;
    private final CoachMapper coachMapper;

    public CoachController(CoachService coachService, CoachMapper coachMapper) {
        this.coachService = coachService;
        this.coachMapper = coachMapper;
    }

    @GetMapping("/flattened")
    public ResponseEntity<List<CoachListDTO>> getAllFlattened() {
        return ResponseEntity.ok(coachService.findAllFlattened());
    }

    @GetMapping("/by-location/{locationId}")
    public ResponseEntity<List<CoachListDTO>> getByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(coachService.findByLocationId(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachDTO> getCoachById(@PathVariable Long id) {
        return coachService.findById(id)
                .map(coachMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CoachDTO> createCoach(@RequestBody CreateUpdateCoachDTO dto) {
        CoachEntity saved = coachService.createCoach(dto);
        return ResponseEntity.ok(coachMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachDTO> updateCoach(@PathVariable Long id, @RequestBody CreateUpdateCoachDTO dto) {
        CoachEntity updated = coachService.updateCoach(id, dto);
        return ResponseEntity.ok(coachMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        coachService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(coachService.getTotalCount());
    }

}
