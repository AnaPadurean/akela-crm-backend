package com.example.akela.swim.crm.service.children;

import com.example.akela.swim.crm.repository.ChildrenRepository;
import org.springframework.stereotype.Service;

@Service
public class ChildrenStatsService {

    private final ChildrenRepository childrenRepository;

    public ChildrenStatsService(ChildrenRepository childrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    public long getActiveChildrenCount() {
        return childrenRepository.countByStatusTrue();
    }
}
