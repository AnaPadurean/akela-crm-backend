package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ChildCoachAssocEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildCoachAssocRepository extends JpaRepository<ChildCoachAssocEntity, Long> {
    List<ChildCoachAssocEntity> findByCoach_CoachId(Long coachId);
    boolean existsByCoach_CoachIdAndChild_ChildId(Long coachId, Long childId);
}
