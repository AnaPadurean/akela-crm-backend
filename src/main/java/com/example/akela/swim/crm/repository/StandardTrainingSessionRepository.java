package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.StandardTrainingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StandardTrainingSessionRepository
        extends JpaRepository<StandardTrainingSessionEntity, Long> {

    List<StandardTrainingSessionEntity> findByCoachId(Long coachId);


}
