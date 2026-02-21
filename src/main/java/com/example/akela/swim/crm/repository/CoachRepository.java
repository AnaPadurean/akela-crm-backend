package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.CoachEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<CoachEntity, Long> {
    Optional<CoachEntity> findByCoachId(Long coachId);

    List<CoachEntity> findAllByCoachIdIn(Collection<Long> coachIds);
    long count();

}
