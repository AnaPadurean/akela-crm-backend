package com.example.akela.swim.crm.service.children;

import com.example.akela.swim.crm.dto.children.ChildWithParentsDTO;
import com.example.akela.swim.crm.dto.parents.ParentAssocDTO;
import com.example.akela.swim.crm.dto.parents.ParentDetailsDTO;
import com.example.akela.swim.crm.dto.SaveChildWithParentsDTO;
import com.example.akela.swim.crm.entity.ChildParentAssocEntity;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import com.example.akela.swim.crm.entity.ParentEntity;
import com.example.akela.swim.crm.service.ParentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ChildWithParentsService {

    private final ChildrenService childrenService;
    private final ParentService parentService;
    private final ChildParentAssocService assocService;

    public ChildWithParentsService(
            ChildrenService childrenService,
            ParentService parentService,
            ChildParentAssocService assocService
    ) {
        this.childrenService = childrenService;
        this.parentService = parentService;
        this.assocService = assocService;
    }

    @Transactional
    public void save(SaveChildWithParentsDTO dto) {

        // 1️⃣ copil
        // 1️⃣ copil
        ChildrenEntity child;
        if (dto.getChild().getChildId() == null) {
            child = new ChildrenEntity();
        } else {
            child = childrenService.findById(dto.getChild().getChildId())
                    .orElseThrow(() -> new RuntimeException("Copil inexistent"));
        }

        child.setChildFirstName(dto.getChild().getChildFirstName());
        child.setChildLastName(dto.getChild().getChildLastName());
        child.setChildUniqueIdentifier(dto.getChild().getChildUniqueIdentifier());
        child.setStatus(dto.getChild().getStatus());

// ===== BIRTHDAY =====
        if (dto.getChild().getRawBirthday() != null &&
                !dto.getChild().getRawBirthday().isBlank()) {

            String raw = dto.getChild().getRawBirthday().trim();
            LocalDate localDate;

            if (raw.matches("\\d{4}")) {
                localDate = LocalDate.of(Integer.parseInt(raw), 1, 1);
            } else if (raw.matches("\\d{2}/\\d{2}/\\d{4}")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                localDate = LocalDate.parse(raw, formatter);
            } else {
                throw new RuntimeException("Invalid birthday format: " + raw);
            }

            child.setBirthday(java.sql.Date.valueOf(localDate));
        } else {
            child.setBirthday(null);
        }

// ✅ SAVE ONCE, WITH BIRTHDAY INCLUDED
        childrenService.save(child);


        // 2️⃣ la EDIT: ștergi asocierile vechi
        assocService.deleteByChildId(child.getChildId());

        // 3️⃣ părinți + asocieri
        for (ParentAssocDTO p : dto.getParents()) {

            ParentEntity parent;
            if (p.getParentId() == null) {
                parent = new ParentEntity();
            } else {
                parent = parentService.findById(p.getParentId())
                        .orElse(new ParentEntity());
            }

            parent.setParentFirstName(p.getParentFirstName());
            parent.setParentLastName(p.getParentLastName());
            parent.setPhone(p.getPhone());
            parent.setEmail(p.getEmail());
            parent.setAddress(p.getAddress());

            parentService.save(parent);

            ChildParentAssocEntity assoc = new ChildParentAssocEntity();
            assoc.setChild(child);
            assoc.setParent(parent);
            assoc.setRelationshipType(p.getRelationshipType());
            assoc.setLegalGuardian(p.isLegalGuardian());

            assocService.save(assoc);
        }


    }

    @Transactional
    public List<ChildWithParentsDTO> getAll() {

        List<ChildrenEntity> children = childrenService.findAll();

        return children.stream().map(child -> {

            List<ChildParentAssocEntity> assocs =
                    assocService.findByChildId(child.getChildId());

            ChildWithParentsDTO dto = new ChildWithParentsDTO();

            dto.setChildId(child.getChildId());
            dto.setChildFirstName(child.getChildFirstName());
            dto.setChildLastName(child.getChildLastName());
            if (child.getBirthday() != null) {
                dto.setBirthday(
                        new java.sql.Date(child.getBirthday().getTime()).toLocalDate()
                );
            }



            dto.setChildUniqueIdentifier(child.getChildUniqueIdentifier());
            dto.setStatus(child.isStatus());

            // ===== PENTRU TABEL =====
            dto.setParents(
                    assocs.stream()
                            .map(a ->
                                    a.getParent().getParentFirstName() + " " +
                                            a.getParent().getParentLastName()
                            )
                            .toList()
            );

            // ===== PENTRU EDIT =====
            dto.setParentsDetails(
                    assocs.stream()
                            .map(a -> {
                                ParentEntity p = a.getParent();
                                return new ParentDetailsDTO(
                                        p.getParentId(),
                                        p.getParentFirstName(),
                                        p.getParentLastName(),
                                        p.getPhone(),
                                        p.getEmail(),
                                        a.getRelationshipType(),
                                        a.isLegalGuardian(),
                                        p.getAddress()
                                );
                            })
                            .toList()
            );

            return dto;

        }).toList();
    }

}

