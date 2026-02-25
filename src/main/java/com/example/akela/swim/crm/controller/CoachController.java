package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.CoachListDTO;
import com.example.akela.swim.crm.dto.CreateUpdateCoachDTO;
import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.service.coach.CoachService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")

public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    // LISTARE PENTRU UI (Coach + locationIds)
    @GetMapping("/flattened")
    public ResponseEntity<List<CoachListDTO>> getAllFlattened() {
        return ResponseEntity.ok(coachService.findAllFlattened());
    }

    // (optional) Coachi per locație
    @GetMapping("/by-location/{locationId}")
    public ResponseEntity<List<CoachListDTO>> getByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(coachService.findByLocationId(locationId));
    }

    // Dacă încă vrei entități brute pentru debugging:
    @GetMapping("/{id}")
    public ResponseEntity<CoachEntity> getCoachById(@PathVariable Long id) {
        return coachService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CoachEntity> createCoach(@RequestBody CreateUpdateCoachDTO dto) {
        return ResponseEntity.ok(coachService.createCoach(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachEntity> updateCoach(@PathVariable Long id, @RequestBody CreateUpdateCoachDTO dto) {
        return ResponseEntity.ok(coachService.updateCoach(id, dto));
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
