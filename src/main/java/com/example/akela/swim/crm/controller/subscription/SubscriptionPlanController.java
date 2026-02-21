package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.CreateSubscriptionPlanDTO;
import com.example.akela.swim.crm.dto.subscription.SubscriptionPlanDTO;
import com.example.akela.swim.crm.entity.SubscriptionPlanEntity;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.service.subscription.SubscriptionPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptionPlans")

public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlans() {
        List<SubscriptionPlanDTO> dtos = subscriptionPlanService.findAll()
                .stream()
                .map(plan -> {
                    SubscriptionPlanDTO dto = new SubscriptionPlanDTO();
                    dto.setSubscriptionPlanId(plan.getSubscriptionPlanId());
                    dto.setSessions(plan.getSessions());
                    dto.setPrice(plan.getPrice());
//                    dto.setValidFrom(plan.getValidFrom());
//                    dto.setValidTo(plan.getValidTo());
                    dto.setActive(plan.isActive());
                    dto.setSubscriptionTypeId(
                            plan.getSubscriptionType() != null ? plan.getSubscriptionType().getSubscriptionTypeId() : null
                    );
                    dto.setSubscriptionTypeName(
                            plan.getSubscriptionType() != null ? plan.getSubscriptionType().getName() : "-"
                    );
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanEntity> getSubscriptionPlansById(@PathVariable Long id) {
        return subscriptionPlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSubscriptionPlan(@RequestBody CreateSubscriptionPlanDTO dto) {
        try {
            SubscriptionTypeEntity type = subscriptionPlanService.findTypeById(dto.getSubscriptionTypeId())
                    .orElseThrow(() -> new RuntimeException("Subscription type not found"));

            SubscriptionPlanEntity plan = new SubscriptionPlanEntity();
            plan.setSubscriptionType(type);
            plan.setSessions(dto.getSessions());
            plan.setPrice(dto.getPrice());
//            plan.setValidFrom(dto.getValidFrom());
//            plan.setValidTo(dto.getValidTo());
            plan.setActive(dto.isActive());

            SubscriptionPlanEntity saved = subscriptionPlanService.save(plan);

            SubscriptionPlanDTO responseDto = new SubscriptionPlanDTO();
            responseDto.setSubscriptionPlanId(saved.getSubscriptionPlanId());
            responseDto.setSessions(saved.getSessions());
            responseDto.setPrice(saved.getPrice());
//            responseDto.setValidFrom(saved.getValidFrom());
//            responseDto.setValidTo(saved.getValidTo());
            responseDto.setActive(saved.isActive());
            responseDto.setSubscriptionTypeId(type.getSubscriptionTypeId());
            responseDto.setSubscriptionTypeName(type.getName());

            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Eroare la salvare: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@PathVariable Long id, @RequestBody CreateSubscriptionPlanDTO dto) {
        return subscriptionPlanService.findById(id)
                .map(existing -> {
                    try {
                        SubscriptionTypeEntity type = subscriptionPlanService.findTypeById(dto.getSubscriptionTypeId())
                                .orElseThrow(() -> new RuntimeException("Subscription type not found"));

                        existing.setSubscriptionType(type);
                        existing.setSessions(dto.getSessions());
                        existing.setPrice(dto.getPrice());
//                        existing.setValidFrom(dto.getValidFrom());
//                        existing.setValidTo(dto.getValidTo());
                        existing.setActive(dto.isActive());

                        SubscriptionPlanEntity updated = subscriptionPlanService.save(existing);

                        SubscriptionPlanDTO responseDto = new SubscriptionPlanDTO();
                        responseDto.setSubscriptionPlanId(updated.getSubscriptionPlanId());
                        responseDto.setSessions(updated.getSessions());
                        responseDto.setPrice(updated.getPrice());
//                        responseDto.setValidFrom(updated.getValidFrom());
//                        responseDto.setValidTo(updated.getValidTo());
                        responseDto.setActive(updated.isActive());
                        responseDto.setSubscriptionTypeId(type.getSubscriptionTypeId());
                        responseDto.setSubscriptionTypeName(type.getName());

                        return ResponseEntity.ok(responseDto);

                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().body("Eroare la actualizare: " + e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long id) {
        subscriptionPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
