package com.example.akela.swim.crm.controller.children;

import com.example.akela.swim.crm.dto.SaveChildWithParentsDTO;
import com.example.akela.swim.crm.service.children.ChildWithParentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/children/with-parents")
public class ChildWithParentsController {

    private final ChildWithParentsService service;

    public ChildWithParentsController(ChildWithParentsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody SaveChildWithParentsDTO dto) {
        service.save(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{childId}")
    public ResponseEntity<Void> update(@PathVariable Long childId, @RequestBody SaveChildWithParentsDTO dto) {
        dto.getChild().setChildId(childId);
        service.save(dto);
        return ResponseEntity.ok().build();
    }
}
