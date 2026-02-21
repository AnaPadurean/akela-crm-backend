package com.example.akela.swim.crm.repository;

import com.example.akela.swim.crm.entity.ChildParentAssocEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildParentAssocRepository extends JpaRepository<ChildParentAssocEntity, Long> {

    List<ChildParentAssocEntity> findByParent_ParentId(Long parentId);
    boolean existsByParent_ParentIdAndChild_ChildId(Long parentId, Long childId);
    void deleteByChild_ChildId(Long childId);
    List<ChildParentAssocEntity> findByChild_ChildId(Long childId);

}
