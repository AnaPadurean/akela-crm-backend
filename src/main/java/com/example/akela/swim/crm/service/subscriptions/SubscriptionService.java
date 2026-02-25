package com.example.akela.swim.crm.service.subscriptions;

import com.example.akela.swim.crm.entity.SubscriptionEntity;
import com.example.akela.swim.crm.repository.SubscriptionRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionEntity> findAll() {
        return subscriptionRepository.findAll();
    }

    public Optional<SubscriptionEntity> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public SubscriptionEntity save(SubscriptionEntity subscriptionEntity) {
        return subscriptionRepository.save(subscriptionEntity);
    }

    public void delete(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public Optional<SubscriptionEntity> findActiveByChildId(Long childId) {
        return subscriptionRepository.findActiveByChildId(childId);
    }

    public Optional<SubscriptionEntity> findLastByChildId(Long childId) {
        return subscriptionRepository.findFirstByChild_ChildIdOrderBySubscriptionIdDesc(childId);
    }

    public Optional<SubscriptionEntity> findMostRecentNegativeByChildId(Long childId) {
        return subscriptionRepository
                .findFirstByChild_ChildIdAndRemainingSessionsLessThanOrderBySubscriptionIdDesc(childId, 0);
    }

    public List<SubscriptionEntity> findActiveByChildIdAll(Long childId) {
        return subscriptionRepository.findByChild_ChildIdAndStatus(childId, "ACTIV");
    }

    public List<SubscriptionEntity> findByChildId(Long childId) {
        return subscriptionRepository.findByChild_ChildId(childId);
    }

    public List<SubscriptionEntity> search(List<Long> childIds, String status, Boolean paid) {
        Specification<SubscriptionEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // childIds IN (...)
            if (childIds != null && !childIds.isEmpty()) {
                predicates.add(root.get("child").get("childId").in(childIds));
            }

            // status filter (ignore if null/empty/ALL)
            if (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // paid filter (ignore if null)
            if (paid != null) {
                predicates.add(cb.equal(root.get("isPaid"), paid));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return subscriptionRepository.findAll(spec);
    }

}
