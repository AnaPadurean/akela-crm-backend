package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ReservationEntity;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("""
        select r.session.sessionId
        from ReservationEntity r
        where r.weeklyTrainingSession.id = :weeklySessionId
          and r.session is not null
    """)
    List<Long> findSessionIdsByWeeklyTrainingSessionId(@Param("weeklySessionId") Long weeklySessionId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ReservationEntity r where r.weeklyTrainingSession.id = :weeklySessionId")
    int deleteAllByWeeklyTrainingSessionId(@Param("weeklySessionId") Long weeklySessionId);

    List<ReservationEntity> findBySubscription_SubscriptionIdOrderByReservationDateTimeAsc(Long subscriptionId);



}
