package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.CoachLocationAssocEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachLocationAssocRepository extends JpaRepository<CoachLocationAssocEntity, Long> {

    boolean existsByCoach_CoachIdAndLocation_LocationId(Long coachId, Long locationId);

    List<CoachLocationAssocEntity> findByCoach_CoachId(Long coachId);

    List<CoachLocationAssocEntity> findByCoach_CoachIdIn(List<Long> coachIds);

    List<CoachLocationAssocEntity> findByLocation_LocationId(Long locationId);

    void deleteByCoach_CoachId(Long coachId);
}
