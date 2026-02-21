package com.example.akela.swim.crm.service.subscription;

import com.example.akela.swim.crm.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionStatsService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionStatsService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public long getActiveSubscriptionsCount() {
        return subscriptionRepository.countByStatusIgnoreCase("ACTIV");
    }
}
