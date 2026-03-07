package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.children.ChildResponseDTO;
import com.example.akela.swim.crm.entity.ChildrenEntity;
import org.springframework.stereotype.Component;

@Component
public class ChildrenMapper {

    public ChildResponseDTO toDto(ChildrenEntity entity) {
        if (entity == null) {
            return null;
        }

        ChildResponseDTO dto = new ChildResponseDTO();
        dto.setChildId(entity.getChildId());
        dto.setChildFirstName(entity.getChildFirstName());
        dto.setChildLastName(entity.getChildLastName());
        dto.setBirthday(entity.getBirthday());
        dto.setChildUniqueIdentifier(entity.getChildUniqueIdentifier());
        dto.setStatus(entity.isStatus());
        return dto;
    }
}
