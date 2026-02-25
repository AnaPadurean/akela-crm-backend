package com.example.akela.swim.crm.service.subscriptions;

import com.example.akela.swim.crm.entity.SubscriptionPlanEntity;
import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.repository.SubscriptionPlanRepository;
import com.example.akela.swim.crm.repository.SubscriptionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public SubscriptionPlanService(SubscriptionPlanRepository subscriptionPlanRepository,
                                   SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    public List<SubscriptionPlanEntity> findAll() {
        return subscriptionPlanRepository.findAll();
    }

    public Optional<SubscriptionPlanEntity> findById(Long id) {
        return subscriptionPlanRepository.findById(id);
    }

    public SubscriptionPlanEntity save(SubscriptionPlanEntity subscriptionPlanEntity) {
        return subscriptionPlanRepository.save(subscriptionPlanEntity);
    }

    public void delete(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }

    public Optional<SubscriptionTypeEntity> findTypeById(Long id) {
        return subscriptionTypeRepository.findById(id);
    }
}
