package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.subscription.SubscriptionTypeDTO;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptionType")

public class SubscriptionTypeDDController {

    private final SubscriptionTypeService subscriptionTypeService;

    public SubscriptionTypeDDController(SubscriptionTypeService subscriptionTypeService) {
        this.subscriptionTypeService = subscriptionTypeService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionTypeDTO>> getAllSubscriptionTypes() {
        List<SubscriptionTypeDTO> dtos = subscriptionTypeService.findAll()
                .stream()
                .map(type -> {
                    SubscriptionTypeDTO dto = new SubscriptionTypeDTO();
                    dto.setSubscriptionTypeId(type.getSubscriptionTypeId());
                    dto.setCode(type.getCode());
                    dto.setName(type.getName());
                    dto.setMaxChildren(type.getMaxChildren());
                    dto.setDescription(type.getDescription());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionTypeEntity> getSubscriptionTypeById(@PathVariable Long id) {
        return subscriptionTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubscriptionTypeEntity> createSubscriptionType(@RequestBody SubscriptionTypeEntity subscriptionTypeEntity) {
        return ResponseEntity.ok(subscriptionTypeService.save(subscriptionTypeEntity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionTypeEntity> updateSubscriptionType(@PathVariable Long id, @RequestBody SubscriptionTypeEntity subscriptionTypeEntity) {
        return subscriptionTypeService.findById(id)
                .map(existing -> {
                    subscriptionTypeEntity.setSubscriptionTypeId(id);
                    return ResponseEntity.ok(subscriptionTypeService.save(subscriptionTypeEntity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionType(@PathVariable Long id) {
        subscriptionTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
