package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.children.ChildCoachAssocResponseDTO;
import com.example.akela.swim.crm.entity.ChildCoachAssocEntity;
import org.springframework.stereotype.Component;

@Component
public class ChildCoachAssocMapper {

    public ChildCoachAssocResponseDTO toDto(ChildCoachAssocEntity entity) {
        if (entity == null) {
            return null;
        }

        ChildCoachAssocResponseDTO dto = new ChildCoachAssocResponseDTO();
        dto.setCoachChildId(entity.getCoachChildId());
        dto.setTrainingType(entity.getTrainingType());

        if (entity.getCoach() != null) {
            dto.setCoachId(entity.getCoach().getCoachId());
            dto.setCoachFirstName(entity.getCoach().getCoachFirstName());
            dto.setCoachLastName(entity.getCoach().getCoachLastName());
        }

        if (entity.getChild() != null) {
            dto.setChildId(entity.getChild().getChildId());
            dto.setChildFirstName(entity.getChild().getChildFirstName());
            dto.setChildLastName(entity.getChild().getChildLastName());
        }

        return dto;
    }
}
