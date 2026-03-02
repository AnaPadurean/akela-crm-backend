package com.example.akela.swim.crm.controller.schedule;

import com.example.akela.swim.crm.dto.ConfirmWeeklySessionDTO;
import com.example.akela.swim.crm.dto.WeeklyTrainingSessionDTO;
import com.example.akela.swim.crm.entity.WeeklyTrainingSessionEntity;
import com.example.akela.swim.crm.service.schedule.WeeklyTrainingSessionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weekly-sessions")

public class WeeklyTrainingSessionController {

    private final WeeklyTrainingSessionService service;

    public WeeklyTrainingSessionController(WeeklyTrainingSessionService service) {
        this.service = service;
    }

    @GetMapping("/generate/{coachId}")
    public List<WeeklyTrainingSessionEntity> generateForCoach(
            @PathVariable Long coachId,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        // Normalizează la LUNI
        weekStart = weekStart.with(DayOfWeek.MONDAY);

        return service.generateWeeklySchedule(coachId, weekStart);
    }

    @PutMapping("/{id}/confirm")
    public WeeklyTrainingSessionEntity confirm(
            @PathVariable Long id,
            @RequestBody ConfirmWeeklySessionDTO dto
    ) {
        return service.confirmSession(id, dto);
    }




    @GetMapping("/coach/{coachId}")
    public List<WeeklyTrainingSessionEntity> getCoachWeekly(
            @PathVariable Long coachId,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        weekStart = weekStart.with(DayOfWeek.MONDAY);

        return service.getWeeklyScheduleForCoach(coachId, weekStart);
    }


    @PostMapping
    public WeeklyTrainingSessionEntity create(
            @RequestBody WeeklyTrainingSessionDTO dto,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        weekStart = weekStart.with(DayOfWeek.MONDAY);

        return service.create(dto, weekStart);
    }


    @PutMapping("/{id}")
    public WeeklyTrainingSessionEntity update(
            @PathVariable Long id,
            @RequestBody WeeklyTrainingSessionDTO dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public List<WeeklyTrainingSessionEntity> getAll() {
        return service.findAll();
    }

    @GetMapping("/generated/{coachId}")
    public boolean isWeekGenerated(
            @PathVariable Long coachId,
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        weekStart = weekStart.with(DayOfWeek.MONDAY);

        return service.wasGeneratedFromStandard(coachId, weekStart);
    }


}
