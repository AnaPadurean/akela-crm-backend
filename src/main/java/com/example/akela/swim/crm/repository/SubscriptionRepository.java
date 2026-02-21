package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.SubscriptionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long>,
        JpaSpecificationExecutor<SubscriptionEntity> {

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.child.childId = :childId AND s.status = 'ACTIV'")
    Optional<SubscriptionEntity> findActiveByChildId(@Param("childId") Long childId);

    //  ultimul abonament după subscriptionId
    Optional<SubscriptionEntity> findFirstByChild_ChildIdOrderBySubscriptionIdDesc(Long childId);

    //  cel mai recent abonament cu deficit
    Optional<SubscriptionEntity> findFirstByChild_ChildIdAndRemainingSessionsLessThanOrderBySubscriptionIdDesc(
            Long childId, Integer remainingSessions
    );

    List<SubscriptionEntity> findByChild_ChildIdAndStatus(Long childId, String status);
    List<SubscriptionEntity> findByChild_ChildId(Long childId);
    @Query("select count(s) from SubscriptionEntity s where lower(s.status) = lower(:status)")
    long countByStatusIgnoreCase(@Param("status") String status);

    @Query("""
      select s from SubscriptionEntity s
      where (s.status is null or s.status = 'ACTIV')
        and s.remainingSessions = 1
    """)
    List<SubscriptionEntity> findActiveWithOneRemaining();
}

