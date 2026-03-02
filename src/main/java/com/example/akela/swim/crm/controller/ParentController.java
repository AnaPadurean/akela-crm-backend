package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.parents.ParentListDTO;
import com.example.akela.swim.crm.dto.parents.ParentWithChildrenDTO;
import com.example.akela.swim.crm.entity.ParentEntity;
import com.example.akela.swim.crm.service.parents.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")

public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public ResponseEntity<List<ParentEntity>> getAllParents() {
        return ResponseEntity.ok(parentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentEntity> getParentById(@PathVariable Long id) {
        return parentService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParentEntity> createParent(@RequestBody ParentEntity parent) {
        return ResponseEntity.ok(parentService.save(parent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentEntity> updateParent(@PathVariable Long id, @RequestBody ParentEntity parentEntity) {
        return parentService.findById(id).map(existing -> {
            parentEntity.setParentId(id);
            return ResponseEntity.ok(parentService.save(parentEntity));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-children")
    public ResponseEntity<List<ParentWithChildrenDTO>> getAllParentsWithChildren() {
        return ResponseEntity.ok(parentService.findAllAsDto());
    }

    @GetMapping("/list")
    public ResponseEntity<List<ParentListDTO>> getAllParentsForList() {
        return ResponseEntity.ok(parentService.findAllForList());
    }

}
