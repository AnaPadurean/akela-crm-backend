package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.dto.ConfirmWeeklySessionDTO;
import com.example.akela.swim.crm.dto.WeeklyTrainingSessionDTO;
import com.example.akela.swim.crm.entity.StandardTrainingSessionEntity;
import com.example.akela.swim.crm.entity.WeeklyTrainingSessionEntity;
import com.example.akela.swim.crm.repository.ReservationRepository;
import com.example.akela.swim.crm.repository.StandardTrainingSessionRepository;
import com.example.akela.swim.crm.repository.WeeklyTrainingSessionRepository;
import com.example.akela.swim.crm.service.children.ChildrenService;
import com.example.akela.swim.crm.service.subscription.SubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class WeeklyTrainingSessionService {

    private final WeeklyTrainingSessionRepository weeklyRepo;
    private final StandardTrainingSessionRepository standardRepo;
    private final ReservationRepository reservationRepo;
    private final ReservationService reservationService;
    private final CoachService coachService;
    private final ChildrenService childrenService;
    private final SubscriptionService subscriptionService;
    private final SessionService sessionService;


    public WeeklyTrainingSessionService(
            WeeklyTrainingSessionRepository weeklyRepo,
            StandardTrainingSessionRepository standardRepo, ReservationRepository reservationRepo, ReservationService reservationService, CoachService coachService, ChildrenService childrenService, SubscriptionService subscriptionService, SessionService sessionService
    ) {
        this.weeklyRepo = weeklyRepo;
        this.standardRepo = standardRepo;
        this.reservationRepo = reservationRepo;
        this.reservationService = reservationService;
        this.coachService = coachService;
        this.childrenService = childrenService;
        this.subscriptionService = subscriptionService;
        this.sessionService = sessionService;
    }

    // Generate weekly from STANDARD
    public List<WeeklyTrainingSessionEntity> generateWeeklySchedule(Long coachId, LocalDate weekStart) {

        // Delete old entries for same week
        List<WeeklyTrainingSessionEntity> old =
                weeklyRepo.findByCoachIdAndWeekStartDate(coachId, weekStart);
        weeklyRepo.deleteAll(old);

        // Get the admin-defined template
        List<StandardTrainingSessionEntity> standardSessions =
                standardRepo.findByCoachId(coachId);

        for (StandardTrainingSessionEntity s : standardSessions) {

            LocalDate targetDate = weekStart.plusDays(s.getDayOfWeek() - 1);

            LocalDateTime start = LocalDateTime.of(targetDate, s.getStartTime());
            LocalDateTime end = LocalDateTime.of(targetDate, s.getEndTime());

            WeeklyTrainingSessionEntity w = new WeeklyTrainingSessionEntity();

            w.setCoachId(s.getCoachId());
            w.setChildIds(s.getChildIds());
            w.setStartDateTime(start);
            w.setEndDateTime(end);
            w.setNotes(s.getNotes());
            w.setSourceStandardId(s.getId());
            w.setManualOverride(false);
            w.setWeekStartDate(weekStart);

            weeklyRepo.save(w);
        }

        return weeklyRepo.findByCoachIdAndWeekStartDate(coachId, weekStart);
    }

    public List<WeeklyTrainingSessionEntity> getWeeklyScheduleForCoach(Long coachId, LocalDate weekStart) {
        return weeklyRepo.findByCoachIdAndWeekStartDate(coachId, weekStart);
    }

    public WeeklyTrainingSessionEntity create(WeeklyTrainingSessionDTO dto, LocalDate weekStart) {

        WeeklyTrainingSessionEntity entity = new WeeklyTrainingSessionEntity();

        entity.setChildIds(dto.getChildIds());
        entity.setCoachId(dto.getCoachId());
        entity.setNotes(dto.getNotes());
        entity.setWeekStartDate(weekStart);
        entity.setSourceStandardId(null);
        entity.setManualOverride(true);

        entity.setStartDateTime(dto.getStartDateTime());
        entity.setEndDateTime(dto.getEndDateTime());

        return weeklyRepo.save(entity);
    }

    public WeeklyTrainingSessionEntity update(Long id, WeeklyTrainingSessionDTO dto) {

        WeeklyTrainingSessionEntity entity = weeklyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Weekly session not found " + id));

        entity.setChildIds(dto.getChildIds());
        entity.setCoachId(dto.getCoachId());
        entity.setNotes(dto.getNotes());
        entity.setManualOverride(true);

        entity.setStartDateTime(dto.getStartDateTime());
        entity.setEndDateTime(dto.getEndDateTime());

        return weeklyRepo.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepo.deleteByWeeklyTrainingSession_Id(id);
        weeklyRepo.deleteById(id);
    }


    public List<WeeklyTrainingSessionEntity> findAll() {
        return weeklyRepo.findAll();
    }

    public boolean wasGeneratedFromStandard(Long coachId, LocalDate weekStart) {
        return weeklyRepo.existsGenerated(coachId, weekStart);
    }

    public Optional<WeeklyTrainingSessionEntity> findById(Long id) {
        return weeklyRepo.findById(id);
    }



    @Transactional
    public WeeklyTrainingSessionEntity confirmSession(Long weeklyId, ConfirmWeeklySessionDTO dto) {
        WeeklyTrainingSessionEntity weekly = weeklyRepo.findById(weeklyId)
                .orElseThrow(() -> new RuntimeException("Weekly session not found " + weeklyId));

        // UNCONFIRM
        if (!dto.isConfirmed()) {
            reservationRepo.deleteByWeeklyTrainingSession_Id(weeklyId);
            weekly.setConfirmed(false);
            return weeklyRepo.save(weekly);
        }

        // CONFIRM (refaci rezervările)
        reservationRepo.deleteByWeeklyTrainingSession_Id(weeklyId);

        if (dto.getReservations() != null) {
            for (ConfirmWeeklySessionDTO.ChildReservation r : dto.getReservations()) {
                createReservationForWeekly(weekly, r);
            }
        }

        weekly.setConfirmed(true);
        return weeklyRepo.save(weekly);
    }

    private void createReservationForWeekly(
            WeeklyTrainingSessionEntity weekly,
            ConfirmWeeklySessionDTO.ChildReservation r
    ) {
        var coach = coachService.findById(weekly.getCoachId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        var child = childrenService.findById(r.getChildId())
                .orElseThrow(() -> new RuntimeException("Child not found"));

        var subscription = subscriptionService.findById(r.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        var session = new com.example.akela.swim.crm.entity.SessionEntity();
        session.setSubscription(subscription);
        session.setScheduledDate(weekly.getStartDateTime());
        session.setCompleted(false);
        session.setCanceled(false);
        session = sessionService.save(session);

        var reservation = new com.example.akela.swim.crm.entity.ReservationEntity();
        reservation.setCoach(coach);
        reservation.setChild(child);
        reservation.setSubscription(subscription);
        reservation.setSession(session);
        reservation.setWeeklyTrainingSession(weekly);
        reservation.setReservationDateTime(weekly.getStartDateTime());
        reservation.setStatus(r.getStatus() != null ? r.getStatus() : "PROGRAMATĂ");

        var saved = reservationService.save(reservation);

        session.setReservation(saved);
        sessionService.save(session);
    }








}
