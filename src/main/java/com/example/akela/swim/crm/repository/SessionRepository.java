package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    // după coach
    List<SessionEntity> findByReservation_Coach_CoachId(Long coachId);

    // după interval
    List<SessionEntity> findByScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            LocalDateTime start, LocalDateTime end
    );

    // după coach + interval (coach-ul din rezervare)
    List<SessionEntity> findByReservation_Coach_CoachIdAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Long coachId, LocalDateTime start, LocalDateTime end
    );

    List<SessionEntity> findBySubscription_Coach_CoachIdAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Long coachId, LocalDateTime start, LocalDateTime end
    );

    @Query(value = """
        WITH months AS (
          SELECT generate_series(
            date_trunc('month', make_date(:year, 1, 1)),
            date_trunc('month', make_date(:year, 12, 1)),
            interval '1 month'
          ) AS month_start
        )
        SELECT
          EXTRACT(MONTH FROM m.month_start) AS month,
          COALESCE(COUNT(s.session_id), 0) AS count
        FROM months m
        LEFT JOIN akl_sessions s
          ON date_trunc('month', s.scheduled_date) = m.month_start
         AND s.completed = true
         AND (s.canceled IS NULL OR s.canceled = false)
        GROUP BY m.month_start
        ORDER BY m.month_start
        """, nativeQuery = true)
    List<Object[]> countCompletedSessionsByMonthForYear(@Param("year") int year);
}

