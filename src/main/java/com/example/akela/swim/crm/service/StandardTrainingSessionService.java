package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.dto.StandardTrainingSessionDTO;
import com.example.akela.swim.crm.entity.StandardTrainingSessionEntity;
import com.example.akela.swim.crm.repository.StandardScheduleBlockRepository;
import com.example.akela.swim.crm.repository.StandardTrainingSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandardTrainingSessionService {
    private final StandardScheduleBlockRepository blockRepository;
    private final StandardTrainingSessionRepository repository;

    public StandardTrainingSessionService(StandardTrainingSessionRepository repository, StandardScheduleBlockRepository blockRepository) {
        this.repository = repository;
        this.blockRepository = blockRepository;
    }

    private void assertNotBlocked(StandardTrainingSessionDTO dto) {
        boolean blocked = blockRepository.existsOverlap(
                dto.getCoachId(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime()
        );
        if (blocked) {
            throw new RuntimeException("SLOT_BLOCKED");
        }
    }


    public StandardTrainingSessionEntity create(StandardTrainingSessionDTO dto) {
        assertNotBlocked(dto);
        StandardTrainingSessionEntity entity = new StandardTrainingSessionEntity();

        entity.setChildIds(dto.getChildIds());
        entity.setCoachId(dto.getCoachId());
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setNotes(dto.getNotes());

        return repository.save(entity);
    }

    public StandardTrainingSessionEntity update(Long id, StandardTrainingSessionDTO dto) {
        assertNotBlocked(dto);
        StandardTrainingSessionEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StandardTrainingSession not found: " + id));

        entity.setChildIds(dto.getChildIds());
        entity.setCoachId(dto.getCoachId());
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setNotes(dto.getNotes());

        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Session not found: " + id);
        }
        repository.deleteById(id);
    }

    public List<StandardTrainingSessionEntity> findAll() {
        return repository.findAll();
    }
}
