package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.parents.ParentDTO;
import com.example.akela.swim.crm.entity.ParentEntity;
import org.springframework.stereotype.Component;

@Component
public class ParentMapper {

    public ParentDTO toDto(ParentEntity entity) {
        if (entity == null) {
            return null;
        }

        ParentDTO dto = new ParentDTO();
        dto.setParentId(entity.getParentId());
        dto.setParentFirstName(entity.getParentFirstName());
        dto.setParentLastName(entity.getParentLastName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        return dto;
    }
}
