package com.example.akela.swim.crm.controller.children;

import com.example.akela.swim.crm.dto.children.ChildCoachAssocDTO;
import com.example.akela.swim.crm.dto.children.ChildCoachAssocResponseDTO;
import com.example.akela.swim.crm.dto.children.CreateChildCoachAssocDTO;
import com.example.akela.swim.crm.entity.ChildCoachAssocEntity;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import com.example.akela.swim.crm.entity.CoachEntity;
import com.example.akela.swim.crm.mapper.ChildCoachAssocMapper;
import com.example.akela.swim.crm.service.children.ChildCoachAssocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/childCoachAssoc")

public class ChildCoachAssocController {

    private final ChildCoachAssocService service;
    private final ChildCoachAssocMapper childCoachAssocMapper;

    public ChildCoachAssocController(ChildCoachAssocService service, ChildCoachAssocMapper childCoachAssocMapper) {
        this.service = service;
        this.childCoachAssocMapper = childCoachAssocMapper;
    }

    @GetMapping
    public ResponseEntity<List<ChildCoachAssocResponseDTO>> getAll() {
        List<ChildCoachAssocResponseDTO> dtos = service.findAll().stream()
                .map(childCoachAssocMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildCoachAssocResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(childCoachAssocMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateChildCoachAssocDTO dto) {
        try {
            if (dto.getCoachId() == null || dto.getChildId() == null) {
                return ResponseEntity.badRequest().body("CoachId și childId sunt obligatorii!");
            }

            if (service.existsByCoachIdAndChildId(dto.getCoachId(), dto.getChildId())) {
                return ResponseEntity.status(409).body("Această asociere există deja!");
            }

            var assoc = new ChildCoachAssocEntity();
            var coach = new CoachEntity();
            coach.setCoachId(dto.getCoachId());

            var child = new ChildrenEntity();
            child.setChildId(dto.getChildId());

            assoc.setCoach(coach);
            assoc.setChild(child);
            assoc.setTrainingType(dto.getTrainingType());

            var saved = service.save(assoc);
            return ResponseEntity.ok(childCoachAssocMapper.toDto(saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Eroare la salvare: " + e.getMessage());
        }
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<ChildCoachAssocDTO>> getByCoachId(@PathVariable Long coachId) {
        List<ChildCoachAssocDTO> result = service.findByCoachId(coachId)
                .stream()
                .map(assoc -> new ChildCoachAssocDTO(
                        assoc.getCoachChildId(),
                        assoc.getChild() != null ? assoc.getChild().getChildId() : null,
                        assoc.getTrainingType(),
                        assoc.getChild() != null ? assoc.getChild().getChildFirstName() : null,
                        assoc.getChild() != null ? assoc.getChild().getChildLastName() : null
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
