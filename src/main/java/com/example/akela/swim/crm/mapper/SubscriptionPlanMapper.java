package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.subscription.SubscriptionPlanDTO;
import com.example.akela.swim.crm.entity.SubscriptionPlanEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlanDTO toDto(SubscriptionPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        SubscriptionPlanDTO dto = new SubscriptionPlanDTO();
        dto.setSubscriptionPlanId(entity.getSubscriptionPlanId());
        dto.setSessions(entity.getSessions());
        dto.setPrice(entity.getPrice());
        dto.setActive(entity.isActive());

        if (entity.getSubscriptionType() != null) {
            dto.setSubscriptionTypeId(entity.getSubscriptionType().getSubscriptionTypeId());
            dto.setSubscriptionTypeName(entity.getSubscriptionType().getName());
        }

        return dto;
    }
}
