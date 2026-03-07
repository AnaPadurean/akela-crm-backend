package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.CreateSubscriptionPlanDTO;
import com.example.akela.swim.crm.dto.subscription.SubscriptionPlanDTO;
import com.example.akela.swim.crm.entity.SubscriptionPlanEntity;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.mapper.SubscriptionPlanMapper;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptionPlans")

public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    public SubscriptionPlanController(
            SubscriptionPlanService subscriptionPlanService,
            SubscriptionPlanMapper subscriptionPlanMapper
    ) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlans() {
        List<SubscriptionPlanDTO> dtos = subscriptionPlanService.findAll().stream()
                .map(subscriptionPlanMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlansById(@PathVariable Long id) {
        return subscriptionPlanService.findById(id)
                .map(subscriptionPlanMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSubscriptionPlan(@RequestBody CreateSubscriptionPlanDTO dto) {
        try {
            SubscriptionTypeEntity type = subscriptionPlanService.findTypeById(dto.getSubscriptionTypeId()).orElseThrow(() -> new RuntimeException("Subscription type not found"));

            SubscriptionPlanEntity plan = new SubscriptionPlanEntity();
            plan.setSubscriptionType(type);
            plan.setSessions(dto.getSessions());
            plan.setPrice(dto.getPrice());
            plan.setActive(dto.isActive());

            SubscriptionPlanEntity saved = subscriptionPlanService.save(plan);
            return ResponseEntity.ok(subscriptionPlanMapper.toDto(saved));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Eroare la salvare: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@PathVariable Long id, @RequestBody CreateSubscriptionPlanDTO dto) {
        return subscriptionPlanService.findById(id).map(existing -> {
            try {
                SubscriptionTypeEntity type = subscriptionPlanService.findTypeById(dto.getSubscriptionTypeId()).orElseThrow(() -> new RuntimeException("Subscription type not found"));

                existing.setSubscriptionType(type);
                existing.setSessions(dto.getSessions());
                existing.setPrice(dto.getPrice());
                existing.setActive(dto.isActive());

                SubscriptionPlanEntity updated = subscriptionPlanService.save(existing);
                return ResponseEntity.ok(subscriptionPlanMapper.toDto(updated));

            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Eroare la actualizare: " + e.getMessage());
            }
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
