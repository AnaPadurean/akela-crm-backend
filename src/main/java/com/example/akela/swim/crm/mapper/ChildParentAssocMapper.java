package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.children.ChildParentAssocResponseDTO;
import com.example.akela.swim.crm.entity.ChildParentAssocEntity;
import org.springframework.stereotype.Component;

@Component
public class ChildParentAssocMapper {

    public ChildParentAssocResponseDTO toDto(ChildParentAssocEntity entity) {
        if (entity == null) {
            return null;
        }

        ChildParentAssocResponseDTO dto = new ChildParentAssocResponseDTO();
        dto.setParentChildId(entity.getParentChildId());
        dto.setRelationshipType(entity.getRelationshipType());
        dto.setLegalGuardian(entity.isLegalGuardian());

        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getParentId());
            dto.setParentFirstName(entity.getParent().getParentFirstName());
            dto.setParentLastName(entity.getParent().getParentLastName());
        }

        if (entity.getChild() != null) {
            dto.setChildId(entity.getChild().getChildId());
            dto.setChildFirstName(entity.getChild().getChildFirstName());
            dto.setChildLastName(entity.getChild().getChildLastName());
        }

        return dto;
    }
}
