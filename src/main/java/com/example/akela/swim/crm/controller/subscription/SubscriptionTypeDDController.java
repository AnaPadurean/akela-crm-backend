package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.subscription.SubscriptionTypeDTO;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.mapper.SubscriptionTypeMapper;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptionType")

public class SubscriptionTypeDDController {

    private final SubscriptionTypeService subscriptionTypeService;
    private final SubscriptionTypeMapper subscriptionTypeMapper;

    public SubscriptionTypeDDController(
            SubscriptionTypeService subscriptionTypeService,
            SubscriptionTypeMapper subscriptionTypeMapper
    ) {
        this.subscriptionTypeService = subscriptionTypeService;
        this.subscriptionTypeMapper = subscriptionTypeMapper;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionTypeDTO>> getAllSubscriptionTypes() {
        List<SubscriptionTypeDTO> dtos = subscriptionTypeService.findAll().stream()
                .map(subscriptionTypeMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionTypeDTO> getSubscriptionTypeById(@PathVariable Long id) {
        return subscriptionTypeService.findById(id)
                .map(subscriptionTypeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubscriptionTypeDTO> createSubscriptionType(@RequestBody SubscriptionTypeDTO dto) {
        SubscriptionTypeEntity saved = subscriptionTypeService.save(subscriptionTypeMapper.toEntity(dto));
        return ResponseEntity.ok(subscriptionTypeMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionTypeDTO> updateSubscriptionType(@PathVariable Long id, @RequestBody SubscriptionTypeDTO dto) {
        return subscriptionTypeService.findById(id).map(existing -> {
            SubscriptionTypeEntity entity = subscriptionTypeMapper.toEntity(dto);
            entity.setSubscriptionTypeId(id);
            SubscriptionTypeEntity saved = subscriptionTypeService.save(entity);
            return ResponseEntity.ok(subscriptionTypeMapper.toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionType(@PathVariable Long id) {
        subscriptionTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
