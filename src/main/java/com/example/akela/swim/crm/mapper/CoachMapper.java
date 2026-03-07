package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.CoachDTO;
import com.example.akela.swim.crm.entity.CoachEntity;
import org.springframework.stereotype.Component;

@Component
public class CoachMapper {

    public CoachDTO toDto(CoachEntity entity) {
        if (entity == null) {
            return null;
        }

        CoachDTO dto = new CoachDTO();
        dto.setCoachId(entity.getCoachId());
        dto.setCoachFirstName(entity.getCoachFirstName());
        dto.setCoachLastName(entity.getCoachLastName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        return dto;
    }
}
