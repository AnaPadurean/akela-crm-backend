package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.dto.NotificationDTO;
import com.example.akela.swim.crm.entity.SubscriptionEntity;
import com.example.akela.swim.crm.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final SubscriptionRepository subscriptionRepository;

    public NotificationService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<NotificationDTO> getWarnings() {
        List<SubscriptionEntity> subs = subscriptionRepository.findActiveWithOneRemaining();

        return subs.stream().map(s -> {
            NotificationDTO n = new NotificationDTO();

            String childName = s.getChild() != null
                    ? s.getChild().getChildLastName() + " " + s.getChild().getChildFirstName()
                    : "Copil";

            n.setMessage(childName + " mai are 1 ședință rămasă.");
            n.setIcon("warning");
            n.setColor("warn");
            n.setRoute("/subscriptions");
            n.setTime(LocalDateTime.now());

            return n;
        }).toList();
    }
}
