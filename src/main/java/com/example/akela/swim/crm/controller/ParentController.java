package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.parents.ParentListDTO;
import com.example.akela.swim.crm.dto.parents.ParentWithChildrenDTO;
import com.example.akela.swim.crm.dto.parents.ParentDTO;
import com.example.akela.swim.crm.entity.ParentEntity;
import com.example.akela.swim.crm.mapper.ParentMapper;
import com.example.akela.swim.crm.service.parents.ParentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")

public class ParentController {

    private final ParentService parentService;
    private final ParentMapper parentMapper;

    public ParentController(ParentService parentService, ParentMapper parentMapper) {
        this.parentService = parentService;
        this.parentMapper = parentMapper;
    }

    @GetMapping
    public ResponseEntity<List<ParentDTO>> getAllParents() {
        List<ParentDTO> dtos = parentService.findAll().stream()
                .map(parentMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDTO> getParentById(@PathVariable Long id) {
        return parentService.findById(id)
                .map(parentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParentDTO> createParent(@RequestBody ParentEntity parent) {
        ParentEntity saved = parentService.save(parent);
        return ResponseEntity.ok(parentMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParentDTO> updateParent(@PathVariable Long id, @RequestBody ParentEntity parentEntity) {
        return parentService.findById(id).map(existing -> {
            parentEntity.setParentId(id);
            ParentEntity saved = parentService.save(parentEntity);
            return ResponseEntity.ok(parentMapper.toDto(saved));
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
