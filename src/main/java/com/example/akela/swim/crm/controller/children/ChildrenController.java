package com.example.akela.swim.crm.controller.children;

import com.example.akela.swim.crm.dto.children.ChildWithCoachesAndSubscriptionsDTO;
import com.example.akela.swim.crm.dto.children.ChildResponseDTO;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import com.example.akela.swim.crm.mapper.ChildrenMapper;
import com.example.akela.swim.crm.service.children.ChildCoachAssocService;
import com.example.akela.swim.crm.service.children.ChildrenService;
import com.example.akela.swim.crm.service.children.ChildrenStatsService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/children")

public class ChildrenController {

    private final ChildrenService childrenService;
    private final SubscriptionService subscriptionService;
    private final ChildrenStatsService childrenStatsService;
    private final ChildrenMapper childrenMapper;
    private final ChildCoachAssocService childCoachAssocService;

    public ChildrenController(
            ChildrenService childrenService,
            SubscriptionService subscriptionService,
            ChildrenStatsService childrenStatsService,
            ChildrenMapper childrenMapper,
            ChildCoachAssocService childCoachAssocService
    ) {
        this.childrenService = childrenService;
        this.subscriptionService = subscriptionService;
        this.childrenStatsService = childrenStatsService;
        this.childrenMapper = childrenMapper;
        this.childCoachAssocService = childCoachAssocService;
    }

    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> getActiveChildrenCount() {
        return ResponseEntity.ok(childrenStatsService.getActiveChildrenCount());
    }


    @GetMapping("/with-coaches-and-subscriptions")
    public ResponseEntity<List<ChildWithCoachesAndSubscriptionsDTO>> getAllChildrenWithCoachesAndSubscriptions() {
        List<ChildrenEntity> children = childrenService.findAll();

        List<ChildWithCoachesAndSubscriptionsDTO> dtos = children.stream().map(child -> {
            ChildWithCoachesAndSubscriptionsDTO dto = new ChildWithCoachesAndSubscriptionsDTO();
            dto.setChildId(child.getChildId());
            dto.setChildFirstName(child.getChildFirstName());
            dto.setChildLastName(child.getChildLastName());
            dto.setBirthday(child.getBirthday());

            List<Map<String, Object>> coachList = childCoachAssocService.findByChildId(child.getChildId()).stream()
                    .filter(cc -> cc.getCoach() != null)
                    .map(cc -> {
                Map<String, Object> map = new HashMap<>();
                map.put("coachId", cc.getCoach().getCoachId());
                map.put("coachFullName", cc.getCoach().getCoachLastName() + " " + cc.getCoach().getCoachFirstName());
                return map;
            }).collect(Collectors.toList());

            dto.setCoachChildren(coachList);

            List<Map<String, Object>> subscriptionList = subscriptionService.findByChildId(child.getChildId()).stream().filter(sub -> sub.getStatus().equalsIgnoreCase("ACTIV")).map(sub -> {
                Map<String, Object> map = new HashMap<>();
                map.put("subscriptionId", sub.getSubscriptionId());
                map.put("planName", sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe / " + sub.getPlan().getPrice() + " lei)");
                map.put("remainingSessions", sub.getRemainingSessions());
                map.put("status", sub.getStatus());
                map.put("totalSessions", sub.getPlan().getSessions());
                return map;
            }).collect(Collectors.toList());

            dto.setSubscriptions(subscriptionList);

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<ChildResponseDTO>> getAllChildren() {
        List<ChildResponseDTO> dtos = childrenService.findAll().stream()
                .map(childrenMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildResponseDTO> getChildById(@PathVariable Long id) {
        return childrenService.findById(id)
                .map(childrenMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ChildResponseDTO> createChild(@RequestBody ChildrenEntity child) {
        ChildrenEntity saved = childrenService.save(child);
        return ResponseEntity.ok(childrenMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildResponseDTO> updateChild(@PathVariable Long id, @RequestBody ChildrenEntity updatedChild) {
        return childrenService.findById(id).map(existing -> {
            updatedChild.setChildId(id);
            ChildrenEntity saved = childrenService.save(updatedChild);
            return ResponseEntity.ok(childrenMapper.toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        childrenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
