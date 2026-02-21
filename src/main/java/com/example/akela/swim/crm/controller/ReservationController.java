package com.example.akela.swim.crm.controller;

import com.example.akela.swim.crm.dto.ReservationDTO;
import com.example.akela.swim.crm.entity.*;
import com.example.akela.swim.crm.service.*;
import com.example.akela.swim.crm.service.children.ChildrenService;
import com.example.akela.swim.crm.service.subscription.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")

public class ReservationController {

    private final ReservationService reservationService;
    private final CoachService coachService;
    private final ChildrenService childrenService;
    private final SessionService sessionService;
    private final SubscriptionService subscriptionService;
    private final WeeklyTrainingSessionService weeklyTrainingSessionService;

    public ReservationController(
            ReservationService reservationService,
            CoachService coachService,
            ChildrenService childrenService,
            SessionService sessionService,
            SubscriptionService subscriptionService,
            WeeklyTrainingSessionService weeklyTrainingSessionService
    ) {
        this.reservationService = reservationService;
        this.coachService = coachService;
        this.childrenService = childrenService;
        this.sessionService = sessionService;
        this.subscriptionService = subscriptionService;
        this.weeklyTrainingSessionService = weeklyTrainingSessionService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> dtos = reservationService.findAll()
                .stream()
                .map(r -> {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setReservationId(r.getReservationId());
                    dto.setReservationDateTime(r.getReservationDateTime());
                    dto.setStatus(r.getStatus());
                    dto.setSessionId(r.getSession() != null ? r.getSession().getSessionId() : null);

                    if (r.getCoach() != null) {
                        dto.setCoachId(r.getCoach().getCoachId());
                        dto.setCoachFullName(r.getCoach().getCoachLastName() + " " + r.getCoach().getCoachFirstName());
                    }

                    if (r.getChild() != null) {
                        dto.setChildId(r.getChild().getChildId());
                        dto.setChildFullName(r.getChild().getChildLastName() + " " + r.getChild().getChildFirstName());
                    }

                    if (r.getSubscription() != null) {
                        dto.setSubscriptionId(r.getSubscription().getSubscriptionId());
                        dto.setSubscriptionName(
                                r.getSubscription().getPlan().getSubscriptionType().getName() +
                                        " (" + r.getSubscription().getPlan().getSessions() + " ședințe)"
                        );
                    }

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO dto) {
        try {
            CoachEntity coach = coachService.findById(dto.getCoachId())
                    .orElseThrow(() -> new RuntimeException("Coach not found"));

            ChildrenEntity child = childrenService.findById(dto.getChildId())
                    .orElseThrow(() -> new RuntimeException("Child not found"));

            SubscriptionEntity subscription = subscriptionService.findById(dto.getSubscriptionId())
                    .orElseThrow(() -> new RuntimeException("Abonamentul selectat nu există"));

            WeeklyTrainingSessionEntity weekly = null;
            if (dto.getWeeklyTrainingSessionId() != null) {
                weekly = weeklyTrainingSessionService.findById(dto.getWeeklyTrainingSessionId())
                        .orElseThrow(() -> new RuntimeException("Weekly session not found"));
            }

            SessionEntity session = new SessionEntity();
            session.setSubscription(subscription);
            session.setScheduledDate(dto.getReservationDateTime());
            session.setCompleted(false);
            session.setCanceled(false);
            session = sessionService.save(session);

            ReservationEntity reservation = new ReservationEntity();
            reservation.setCoach(coach);
            reservation.setChild(child);
            reservation.setSubscription(subscription);
            reservation.setSession(session);
            reservation.setReservationDateTime(dto.getReservationDateTime());
            reservation.setStatus(dto.getStatus() != null ? dto.getStatus() : "PROGRAMATĂ");
            reservation.setWeeklyTrainingSession(weekly);

            ReservationEntity saved = reservationService.save(reservation);

            session.setReservation(saved);
            sessionService.save(session);

            ReservationDTO response = new ReservationDTO();
            response.setReservationId(saved.getReservationId());
            response.setCoachId(coach.getCoachId());
            response.setCoachFullName(coach.getCoachLastName() + " " + coach.getCoachFirstName());
            response.setChildId(child.getChildId());
            response.setChildFullName(child.getChildLastName() + " " + child.getChildFirstName());
            response.setReservationDateTime(saved.getReservationDateTime());
            response.setStatus(saved.getStatus());
            response.setSessionId(session.getSessionId());
            response.setSubscriptionId(subscription.getSubscriptionId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Eroare la salvare rezervare: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO dto) {
        return reservationService.findById(id)
                .map(existing -> {
                    if (dto.getCoachId() != null) {
                        coachService.findById(dto.getCoachId())
                                .ifPresent(existing::setCoach);
                    }
                    if (dto.getChildId() != null) {
                        childrenService.findById(dto.getChildId())
                                .ifPresent(existing::setChild);
                    }
                    if (dto.getReservationDateTime() != null) {
                        existing.setReservationDateTime(dto.getReservationDateTime());
                    }
                    if (dto.getStatus() != null) {
                        existing.setStatus(dto.getStatus());
                    }

                    ReservationEntity updated = reservationService.save(existing);

                    ReservationDTO response = new ReservationDTO();
                    response.setReservationId(updated.getReservationId());
                    response.setReservationDateTime(updated.getReservationDateTime());
                    response.setStatus(updated.getStatus());
                    if (updated.getCoach() != null) {
                        response.setCoachId(updated.getCoach().getCoachId());
                        response.setCoachFullName(
                                updated.getCoach().getCoachLastName() + " " + updated.getCoach().getCoachFirstName()
                        );
                    }
                    if (updated.getChild() != null) {
                        response.setChildId(updated.getChild().getChildId());
                        response.setChildFullName(
                                updated.getChild().getChildLastName() + " " + updated.getChild().getChildFirstName()
                        );
                    }
                    if (updated.getSession() != null) {
                        response.setSessionId(updated.getSession().getSessionId());
                    }

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservationDTO>> filterReservations(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
            // childId și childIds – ȘTERSE DEFINITIV
    ) {
        String start = (startDate != null && !startDate.isBlank()) ? startDate : null;
        String end   = (endDate != null && !endDate.isBlank()) ? endDate : null;

        List<ReservationEntity> reservations = reservationService.filter(start, end, null);

        List<ReservationDTO> dtos = reservations.stream()
                .map(r -> {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setReservationId(r.getReservationId());
                    dto.setReservationDateTime(r.getReservationDateTime());
                    dto.setStatus(r.getStatus());

                    if (r.getCoach() != null) {
                        dto.setCoachId(r.getCoach().getCoachId());
                        dto.setCoachFullName(r.getCoach().getCoachLastName() + " " + r.getCoach().getCoachFirstName());
                    }

                    if (r.getChild() != null) {
                        dto.setChildId(r.getChild().getChildId());
                        dto.setChildFullName(r.getChild().getChildLastName() + " " + r.getChild().getChildFirstName());
                    }

                    if (r.getSubscription() != null) {
                        dto.setSubscriptionId(r.getSubscription().getSubscriptionId());
                        dto.setSubscriptionName(
                                r.getSubscription().getPlan().getSubscriptionType().getName() +
                                        " (" + r.getSubscription().getPlan().getSessions() + " ședințe)"
                        );
                    }

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-weekly/{weeklyId}")
    public ResponseEntity<List<ReservationDTO>> getByWeekly(@PathVariable Long weeklyId) {

        List<ReservationDTO> dtos = reservationService.findByWeeklyTrainingSessionId(weeklyId)
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    private ReservationDTO toDto(ReservationEntity r) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(r.getReservationId());
        dto.setReservationDateTime(r.getReservationDateTime());
        dto.setStatus(r.getStatus());
        dto.setSessionId(r.getSession() != null ? r.getSession().getSessionId() : null);

        if (r.getCoach() != null) {
            dto.setCoachId(r.getCoach().getCoachId());
            dto.setCoachFullName(r.getCoach().getCoachLastName() + " " + r.getCoach().getCoachFirstName());
        }

        if (r.getChild() != null) {
            dto.setChildId(r.getChild().getChildId());
            dto.setChildFullName(r.getChild().getChildLastName() + " " + r.getChild().getChildFirstName());
        }

        if (r.getSubscription() != null) {
            dto.setSubscriptionId(r.getSubscription().getSubscriptionId());
            dto.setSubscriptionName(
                    r.getSubscription().getPlan().getSubscriptionType().getName() +
                            " (" + r.getSubscription().getPlan().getSessions() + " ședințe)"
            );
        }

        if (r.getWeeklyTrainingSession() != null) {
            dto.setWeeklyTrainingSessionId(r.getWeeklyTrainingSession().getId());
        }

        return dto;
    }

    @GetMapping("/by-subscription/{subscriptionId}")
    public ResponseEntity<List<ReservationDTO>> getBySubscription(@PathVariable Long subscriptionId) {
        List<ReservationDTO> dtos = reservationService.findBySubscriptionId(subscriptionId)
                .stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}
