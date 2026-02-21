package com.example.akela.swim.crm.service.children;

import com.example.akela.swim.crm.entity.ChildCoachAssocEntity;
import com.example.akela.swim.crm.repository.ChildCoachAssocRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChildCoachAssocService {

    private final ChildCoachAssocRepository childCoachAssocRepository;

    public ChildCoachAssocService(ChildCoachAssocRepository childCoachAssocRepository) {
        this.childCoachAssocRepository = childCoachAssocRepository;
    }

    public List<ChildCoachAssocEntity> findAll() {
        return childCoachAssocRepository.findAll();
    }

    public Optional<ChildCoachAssocEntity> findById(Long id) {
        return childCoachAssocRepository.findById(id);
    }

    public ChildCoachAssocEntity save(ChildCoachAssocEntity childCoachAssocEntity) {
        return childCoachAssocRepository.save(childCoachAssocEntity);
    }

    public void delete(Long id) {
        childCoachAssocRepository.deleteById(id);
    }

    public List<ChildCoachAssocEntity> findByCoachId(Long coachId) {
        return childCoachAssocRepository.findByCoach_CoachId(coachId);
    }

    public boolean existsByCoachIdAndChildId(Long coachId, Long childId) {
        return childCoachAssocRepository.existsByCoach_CoachIdAndChild_ChildId(coachId, childId);
    }
}
