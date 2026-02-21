package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.WeeklyTrainingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyTrainingSessionRepository extends JpaRepository<WeeklyTrainingSessionEntity, Long> {

    List<WeeklyTrainingSessionEntity> findByCoachIdAndWeekStartDate(Long coachId, LocalDate weekStartDate);

    List<WeeklyTrainingSessionEntity> findByWeekStartDate(LocalDate weekStartDate);

    @Query("""
    SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END
    FROM WeeklyTrainingSessionEntity w
    WHERE w.coachId = :coachId
      AND w.weekStartDate = :weekStart
      AND w.manualOverride = FALSE
      AND w.sourceStandardId IS NOT NULL
""")
    boolean existsGenerated(Long coachId, LocalDate weekStart);

}
