package com.example.akela.swim.crm.service.schedule;

import com.example.akela.swim.crm.dto.StandardScheduleBlockBulkDTO;
import com.example.akela.swim.crm.entity.StandardScheduleBlockEntity;
import com.example.akela.swim.crm.repository.StandardScheduleBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StandardScheduleBlockService {

    private final StandardScheduleBlockRepository repo;

    public StandardScheduleBlockService(StandardScheduleBlockRepository repo) {
        this.repo = repo;
    }

    public List<StandardScheduleBlockEntity> findByCoach(Long coachId) {
        return repo.findByCoachId(coachId);
    }

    @Transactional
    public List<StandardScheduleBlockEntity> bulkCreate(StandardScheduleBlockBulkDTO dto) {
        List<StandardScheduleBlockEntity> saved = new ArrayList<>();

        for (var s : dto.getSlots()) {
            // dacă există deja exact slotul -> ignorăm (ca să fie idempotent)
            // unique index te protejează oricum, dar așa e mai user-friendly
            StandardScheduleBlockEntity e = new StandardScheduleBlockEntity();
            e.setCoachId(dto.getCoachId());
            e.setDayOfWeek(s.getDayOfWeek());
            e.setStartTime(s.getStartTime());
            e.setEndTime(s.getEndTime());
            e.setReason(dto.getReason());

            try {
                saved.add(repo.save(e));
            } catch (Exception ignore) {
                // duplicate -> ignore
            }
        }

        return saved;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
