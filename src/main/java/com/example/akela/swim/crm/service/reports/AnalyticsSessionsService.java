package com.example.akela.swim.crm.service.reports;

import com.example.akela.swim.crm.dto.MonthCountDTO;
import com.example.akela.swim.crm.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnalyticsSessionsService {

    private final SessionRepository sessionRepository;

    public AnalyticsSessionsService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<MonthCountDTO> getCompletedSessionsPerMonth(Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();

        List<Object[]> rows = sessionRepository.countCompletedSessionsByMonthForYear(y);

        return rows.stream().map(r -> {
            Integer month = ((Number) r[0]).intValue();
            Long count = ((Number) r[1]).longValue();
            return new MonthCountDTO(month, count);
        }).toList();
    }
}
