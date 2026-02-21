package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.StandardScheduleBlockEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface StandardScheduleBlockRepository extends JpaRepository<StandardScheduleBlockEntity, Long> {

    List<StandardScheduleBlockEntity> findByCoachId(Long coachId);

    @Query("""
      select (count(b) > 0) from StandardScheduleBlockEntity b
      where b.coachId = :coachId
        and b.dayOfWeek = :dayOfWeek
        and b.startTime < :endTime
        and b.endTime > :startTime
    """)
    boolean existsOverlap(
            @Param("coachId") Long coachId,
            @Param("dayOfWeek") Integer dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
