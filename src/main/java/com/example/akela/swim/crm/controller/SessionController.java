package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.MonthCountDTO;
import com.example.akela.swim.crm.dto.SessionDTO;
import com.example.akela.swim.crm.entity.SessionEntity;
import com.example.akela.swim.crm.mapper.SessionMapper;
import com.example.akela.swim.crm.model.SessionDayFilter;
import com.example.akela.swim.crm.service.reports.AnalyticsSessionsService;
import com.example.akela.swim.crm.service.reservations.ReservationService;
import com.example.akela.swim.crm.service.sessions.SessionService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")

public class SessionController {

    private final SessionService sessionService;
    private final SubscriptionService subscriptionService;
    private final ReservationService reservationService;
    private final AnalyticsSessionsService analyticsSessionsService;
    private final SessionMapper sessionMapper;

    public SessionController(
            SessionService sessionService,
            SubscriptionService subscriptionService,
            ReservationService reservationService,
            AnalyticsSessionsService analyticsSessionsService,
            SessionMapper sessionMapper
    ) {
        this.sessionService = sessionService;
        this.subscriptionService = subscriptionService;
        this.reservationService = reservationService;
        this.analyticsSessionsService = analyticsSessionsService;
        this.sessionMapper = sessionMapper;
    }


    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<SessionDTO> dtos = sessionService.findAll().stream().map(sessionMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        return sessionService.findById(id)
                .map(sessionMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@RequestBody SessionEntity sessionEntity) {
        SessionEntity saved = sessionService.save(sessionEntity);
        return ResponseEntity.ok(sessionMapper.toDto(saved));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> markSessionCompleted(@PathVariable Long id) {
        try {
            sessionService.changeStatus(id, true, false);
            return ResponseEntity.ok("Sesiune marcată ca efectuată.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> markSessionCanceled(@PathVariable Long id) {
        try {
            sessionService.changeStatus(id, false, true);
            return ResponseEntity.ok("Sesiune anulată.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByCoach(@PathVariable Long coachId) {
        List<SessionDTO> dtos = sessionService.findAllByCoachId(coachId).stream().map(sessionMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<SessionDTO>> getFiltered(@RequestParam SessionDayFilter filter, @RequestParam(required = false) Long coachId) {
        var range = sessionService.getRange(filter);

        List<SessionEntity> sessions = (coachId != null) ? sessionService.findByCoachAndRange(coachId, range.start(), range.end()) : sessionService.findByRange(range.start(), range.end());

        List<SessionDTO> dtos = sessions.stream().map(sessionMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/stats/completed-per-month")
    public ResponseEntity<List<MonthCountDTO>> completedPerMonth(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(analyticsSessionsService.getCompletedSessionsPerMonth(year));
    }

    @GetMapping("/today-view")
    public ResponseEntity<List<SessionDTO>> todayView() {

        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isCoach = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COACH"));

        List<SessionEntity> sessions;

        if (isAdmin) {
            sessions = sessionService.findTodayAll();
        } else if (isCoach) {
            Long coachId = null;

            Object principal = auth.getPrincipal();
            if (principal instanceof com.example.akela.swim.crm.security.AklUserPrincipal up) {
                coachId = up.getCoachId();
            }

            if (coachId == null) {
                return ResponseEntity.status(403).build();
            }

            sessions = sessionService.findTodayByCoach(coachId);
        } else {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(sessions.stream().map(sessionMapper::toDto).toList());
    }

}
