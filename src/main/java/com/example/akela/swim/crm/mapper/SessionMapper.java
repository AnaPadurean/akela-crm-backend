package com.example.akela.swim.crm.mapper;

import com.example.akela.swim.crm.dto.SessionDTO;
import com.example.akela.swim.crm.entity.SessionEntity;
import com.example.akela.swim.crm.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionDTO toDto(SessionEntity entity) {
        if (entity == null) {
            return null;
        }

        SessionDTO dto = new SessionDTO();
        dto.setSessionId(entity.getSessionId());
        dto.setScheduledDate(entity.getScheduledDate());
        dto.setCompleted(entity.getCompleted());
        dto.setCanceled(entity.getCanceled());

        if (entity.getSubscription() != null) {
            SubscriptionEntity sub = entity.getSubscription();
            dto.setSubscriptionId(sub.getSubscriptionId());
            dto.setSubscriptionStatus(sub.getStatus());
            dto.setTotalSessions(sub.getTotalSessions());
            dto.setCompletedSessions(sub.getCompletedSessions());
            dto.setRemainingSessions(sub.getRemainingSessions());

            if (sub.getChild() != null) {
                dto.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());
            }
        }

        if (entity.getReservation() != null && entity.getReservation().getCoach() != null) {
            dto.setCoachId(entity.getReservation().getCoach().getCoachId());
            dto.setCoachFullName(entity.getReservation().getCoach().getCoachLastName() + " " + entity.getReservation().getCoach().getCoachFirstName());
        } else if (entity.getSubscription() != null && entity.getSubscription().getCoach() != null) {
            dto.setCoachId(entity.getSubscription().getCoach().getCoachId());
            dto.setCoachFullName(entity.getSubscription().getCoach().getCoachLastName() + " " + entity.getSubscription().getCoach().getCoachFirstName());
        }

        return dto;
    }
}
