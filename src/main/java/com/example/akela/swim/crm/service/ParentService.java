package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.dto.parents.ParentListDTO;
import com.example.akela.swim.crm.dto.parents.ParentWithChildrenDTO;
import com.example.akela.swim.crm.entity.ParentEntity;
import com.example.akela.swim.crm.repository.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {

    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    public List<ParentEntity> findAll() {
        return parentRepository.findAll();
    }

    public Optional<ParentEntity> findById(Long id) {
        return parentRepository.findById(id);
    }

    public ParentEntity save(ParentEntity parent) {
        return parentRepository.save(parent);
    }

    public void delete(Long id) {
        parentRepository.deleteById(id);
    }

    public List<ParentWithChildrenDTO> findAllAsDto() {
        List<Object[]> rows = parentRepository.findAllParentsWithChildrenNative();
        return rows.stream().map(r -> new ParentWithChildrenDTO(
                ((Number) r[0]).longValue(),
                (String) r[1],
                (String) r[2],
                (String) r[3],
                (String) r[4],
                (String) r[5],
                List.of(((String) r[6]).split(",\\s*"))
        )).toList();
    }

    public List<ParentListDTO> findAllForList() {
        return parentRepository.findAllForList();
    }

}
