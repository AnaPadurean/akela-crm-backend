package com.example.akela.swim.crm.service.subscriptions;

import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import com.example.akela.swim.crm.repository.SubscriptionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionTypeService {

    private final SubscriptionTypeRepository subscriptionTypeRepository;

    public SubscriptionTypeService(SubscriptionTypeRepository subscriptionTypeRepository) {
        this.subscriptionTypeRepository = subscriptionTypeRepository;
    }

    public List<SubscriptionTypeEntity> findAll() {
        return subscriptionTypeRepository.findAll();
    }

    public Optional<SubscriptionTypeEntity> findById(Long id) {
        return subscriptionTypeRepository.findById(id);
    }

    public SubscriptionTypeEntity save(SubscriptionTypeEntity subscriptionTypeEntity) {
        return subscriptionTypeRepository.save(subscriptionTypeEntity);
    }

    public void delete(Long id) {
        subscriptionTypeRepository.deleteById(id);
    }
}
