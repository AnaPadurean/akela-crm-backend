package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("""
SELECT r FROM ReservationEntity r
WHERE (:childId IS NULL OR r.child.childId = :childId)
AND   (COALESCE(:startDate, r.reservationDateTime) <= r.reservationDateTime)
AND   (COALESCE(:endDate, r.reservationDateTime) >= r.reservationDateTime)
""")
    List<ReservationEntity> filter(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("childId") Long childId
    );

    List<ReservationEntity> findByWeeklyTrainingSession_Id(Long weeklySessionId);
    boolean existsByWeeklyTrainingSession_Id(Long weeklySessionId);
    void deleteByWeeklyTrainingSession_Id(Long weeklySessionId);

    List<ReservationEntity> findBySubscription_SubscriptionIdOrderByReservationDateTimeDesc(Long subscriptionId);



}
