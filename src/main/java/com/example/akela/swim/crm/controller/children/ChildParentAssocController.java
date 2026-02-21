package com.example.akela.swim.crm.controller.children;

import com.example.akela.swim.crm.dto.children.ChildAssocDTO;
import com.example.akela.swim.crm.dto.children.CreateChildParentAssocDTO;
import com.example.akela.swim.crm.entity.ChildParentAssocEntity;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import com.example.akela.swim.crm.entity.ParentEntity;
import com.example.akela.swim.crm.service.children.ChildParentAssocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/childParentAssoc")

public class ChildParentAssocController {

    private final ChildParentAssocService childParentAssocService;

    public ChildParentAssocController(ChildParentAssocService childParentAssocService) {
        this.childParentAssocService = childParentAssocService;
    }

    @GetMapping
    public ResponseEntity<List<ChildParentAssocEntity>> getAllChildParentAssoc() {
        return ResponseEntity.ok(childParentAssocService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildParentAssocEntity> getChildParentAssocById(@PathVariable Long id) {
        return childParentAssocService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createChildParentAssoc(@RequestBody CreateChildParentAssocDTO dto) {
        try {

            if (dto.getParentId() == null || dto.getChildId() == null) {
                return ResponseEntity.badRequest().body("parentId și childId sunt obligatorii!");
            }


            boolean exists = childParentAssocService.existsByParentIdAndChildId(dto.getParentId(), dto.getChildId());
            if (exists) {
                return ResponseEntity.status(409).body("Această asociere există deja!");
            }

            var assoc = new ChildParentAssocEntity();

            var parent = new ParentEntity();
            parent.setParentId(dto.getParentId());

            var child = new ChildrenEntity();
            child.setChildId(dto.getChildId());

            assoc.setParent(parent);
            assoc.setChild(child);
            assoc.setRelationshipType(dto.getRelationshipType());
            assoc.setLegalGuardian(dto.isLegalGuardian());


            var saved = childParentAssocService.save(assoc);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Eroare la salvarea asocierii: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<ChildParentAssocEntity> updateChildParentAssoc(@PathVariable Long id, @RequestBody ChildParentAssocEntity childParentAssocEntity) {
        return childParentAssocService.findById(id)
                .map(existing -> {
                    childParentAssocEntity.setParentChildId(id);
                    return ResponseEntity.ok(childParentAssocService.save(childParentAssocEntity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChildParentAssoc(@PathVariable Long id) {
        childParentAssocService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<ChildAssocDTO>> getByParentId(@PathVariable Long parentId) {
        List<ChildAssocDTO> result = childParentAssocService.findByParentId(parentId)
                .stream()
                .map(assoc -> new ChildAssocDTO(
                        assoc.getParentChildId(),
                        assoc.getChild() != null ? assoc.getChild().getChildId() : null,
                        assoc.getRelationshipType(),
                        assoc.isLegalGuardian(),
                        assoc.getChild() != null ? assoc.getChild().getChildFirstName() : null,
                        assoc.getChild() != null ? assoc.getChild().getChildLastName() : null
                ))
                .toList();

        return ResponseEntity.ok(result);
    }



}
