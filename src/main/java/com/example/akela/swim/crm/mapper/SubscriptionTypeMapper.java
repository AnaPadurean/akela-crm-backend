package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.subscription.SubscriptionTypeDTO;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionTypeMapper {

    public SubscriptionTypeDTO toDto(SubscriptionTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        SubscriptionTypeDTO dto = new SubscriptionTypeDTO();
        dto.setSubscriptionTypeId(entity.getSubscriptionTypeId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setMaxChildren(entity.getMaxChildren());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public SubscriptionTypeEntity toEntity(SubscriptionTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        SubscriptionTypeEntity entity = new SubscriptionTypeEntity();
        entity.setSubscriptionTypeId(dto.getSubscriptionTypeId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setMaxChildren(dto.getMaxChildren());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
