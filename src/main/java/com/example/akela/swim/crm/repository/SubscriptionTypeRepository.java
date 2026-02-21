package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.SubscriptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionTypeEntity, Long> {
}
