package com.example.akela.swim.crm.service.children;

import com.example.akela.swim.crm.entity.ChildParentAssocEntity;
import com.example.akela.swim.crm.repository.ChildParentAssocRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChildParentAssocService {

    private final ChildParentAssocRepository childParentAssocRepository;

    public ChildParentAssocService(ChildParentAssocRepository childParentAssocRepository) {
        this.childParentAssocRepository = childParentAssocRepository;
    }

    public List<ChildParentAssocEntity> findAll() {
        return childParentAssocRepository.findAll();
    }

    public Optional<ChildParentAssocEntity> findById(Long id) {
        return childParentAssocRepository.findById(id);
    }

    public ChildParentAssocEntity save(ChildParentAssocEntity childParentAssocEntity) {
        return childParentAssocRepository.save(childParentAssocEntity);
    }

    public void delete(Long id) {
        childParentAssocRepository.deleteById(id);
    }

    public List<ChildParentAssocEntity> findByParentId(Long parentId) {
        return childParentAssocRepository.findByParent_ParentId(parentId);
    }

    public boolean existsByParentIdAndChildId(Long parentId, Long childId) {
        return childParentAssocRepository.existsByParent_ParentIdAndChild_ChildId(parentId, childId);
    }

    public void deleteByChildId(Long childId) {
        childParentAssocRepository.deleteByChild_ChildId(childId);
    }

    public List<ChildParentAssocEntity> findByChildId(Long childId) {
        return childParentAssocRepository.findByChild_ChildId(childId);
    }




}
