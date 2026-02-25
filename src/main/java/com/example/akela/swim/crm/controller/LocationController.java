package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.entity.LocationEntity;
import com.example.akela.swim.crm.service.locations.LocationService;
import com.example.akela.swim.crm.service.locations.LocationStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")

public class LocationController {

    private final LocationService service;
    private final LocationStatsService statsService;


    public LocationController(LocationService service, LocationStatsService statsService) {
        this.service = service;
        this.statsService = statsService;
    }

    // ========================= GET =========================

    @GetMapping
    public ResponseEntity<List<LocationEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> getActiveLocationsCount() {
        return ResponseEntity.ok(statsService.getActiveLocationsCount());
    }

    // ========================= CREATE =========================

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LocationEntity entity) {

        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("code", "VALIDATION_ERROR", "message", "Name is required")
            );
        }

        String nameNorm = service.normalizeName(entity.getName());

        var existingOpt = service.findByNameNorm(nameNorm);
        if (existingOpt.isPresent()) {
            var existing = existingOpt.get();

            return ResponseEntity.status(409).body(
                    Map.of(
                            "code", "LOCATION_EXISTS",
                            "locationId", existing.getLocationId(),
                            "name", existing.getName(),
                            "active", existing.getActive() != null ? existing.getActive() : false
                    )
            );
        }

        entity.setNameNorm(nameNorm);
        if (entity.getActive() == null) {
            entity.setActive(true);
        }

        return ResponseEntity.ok(service.save(entity));
    }

    // ========================= UPDATE =========================

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LocationEntity entity) {

        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("code", "VALIDATION_ERROR", "message", "Name is required")
            );
        }

        var existing = service.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        String nameNorm = service.normalizeName(entity.getName());

        // verifici dacă redenumește într-un nume folosit de ALTĂ locație
        var sameNameOpt = service.findByNameNorm(nameNorm);
        if (sameNameOpt.isPresent() && !sameNameOpt.get().getLocationId().equals(id)) {
            var other = sameNameOpt.get();

            return ResponseEntity.status(409).body(
                    Map.of(
                            "code", "LOCATION_EXISTS",
                            "locationId", other.getLocationId(),
                            "name", other.getName(),
                            "active", other.getActive() != null ? other.getActive() : false
                    )
            );
        }

        existing.setName(entity.getName());
        existing.setNameNorm(nameNorm);
        existing.setAddress(entity.getAddress());
        existing.setActive(entity.getActive());

        return ResponseEntity.ok(service.save(existing));
    }

    // ========================= DELETE =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
