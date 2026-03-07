package com.example.akela.swim.crm.service.sessions;

import com.example.akela.swim.crm.entity.ReservationEntity;
import com.example.akela.swim.crm.entity.SessionEntity;
import com.example.akela.swim.crm.entity.SubscriptionEntity;
import com.example.akela.swim.crm.repository.SessionRepository;
import com.example.akela.swim.crm.service.reservations.ReservationService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionService;
import org.springframework.stereotype.Service;
import com.example.akela.swim.crm.model.SessionDayFilter;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SubscriptionService subscriptionService;
    private final ReservationService reservationService;

    public record DateRange(LocalDateTime start, LocalDateTime end) {}

    public SessionService(SessionRepository sessionRepository, SubscriptionService subscriptionService, ReservationService reservationService) {
        this.sessionRepository = sessionRepository;
        this.subscriptionService = subscriptionService;
        this.reservationService = reservationService;
    }

    public List<SessionEntity> findAll() {
        return sessionRepository.findAll();
    }

    public Optional<SessionEntity> findById(Long id) {
        return sessionRepository.findById(id);
    }

    public SessionEntity save(SessionEntity sessionEntity) {
        return sessionRepository.save(sessionEntity);
    }

    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        sessionRepository.deleteAllByIdInBatch(ids);
    }

    public List<SessionEntity> findAllByCoachId(Long coachId) {
        return sessionRepository.findByReservation_Coach_CoachId(coachId);
    }

    public DateRange getRange(SessionDayFilter filter) {
        LocalDate today = LocalDate.now(); // server time
        return switch (filter) {
            case TODAY -> new DateRange(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
            case YESTERDAY -> new DateRange(today.minusDays(1).atStartOfDay(), today.atStartOfDay());
            case TOMORROW -> new DateRange(today.plusDays(1).atStartOfDay(), today.plusDays(2).atStartOfDay());
            case MONTHLY -> {
                LocalDate firstDay = today.withDayOfMonth(1);
                LocalDate firstDayNextMonth = firstDay.plusMonths(1);
                yield new DateRange(firstDay.atStartOfDay(), firstDayNextMonth.atStartOfDay());
            }
        };
    }

    public List<SessionEntity> findByRange(LocalDateTime start, LocalDateTime end) {
        return sessionRepository
                .findByScheduledDateGreaterThanEqualAndScheduledDateLessThan(start, end);
    }

    public List<SessionEntity> findByCoachAndRange(Long coachId, LocalDateTime start, LocalDateTime end) {
        return sessionRepository
                .findByReservation_Coach_CoachIdAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(coachId, start, end);
    }


    @Transactional
    public void changeStatus(Long sessionId, boolean newCompleted, boolean newCanceled) {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        boolean oldCompleted = Boolean.TRUE.equals(session.getCompleted());
        boolean oldCanceled = Boolean.TRUE.equals(session.getCanceled());

        // 1) Actualizează sesiunea
        session.setCompleted(newCompleted);
        session.setCanceled(newCanceled);
        sessionRepository.save(session);

        // 2) Ajustează abonamentul doar dacă s-a schimbat completed flag
        SubscriptionEntity sub = session.getSubscription();
        if (sub != null) {
            int completedSessions = sub.getCompletedSessions() != null ? sub.getCompletedSessions() : 0;
            int totalSessions = sub.getTotalSessions() != null ? sub.getTotalSessions() : 0;

            if (!oldCompleted && newCompleted) {
                // NOT completed -> completed : consumă 1
                completedSessions += 1;
            } else if (oldCompleted && !newCompleted) {
                // completed -> NOT completed : refund 1
                completedSessions -= 1;
                // dacă vrei să NU cobori sub 0, decomentează:
                // if (completedSessions < 0) completedSessions = 0;
            }

            sub.setCompletedSessions(completedSessions);
            sub.setRemainingSessions(totalSessions - completedSessions); // poate fi negativ, e ok la tine
            subscriptionService.save(sub);
        }

        // 3) Actualizează reservation status (dacă există)
        ReservationEntity reservation = session.getReservation();
        if (reservation != null) {
            if (newCompleted) {
                reservation.setStatus("EFECTUATĂ");
            } else if (newCanceled) {
                reservation.setStatus("ANULATĂ");
            } else {
                reservation.setStatus("PROGRAMATĂ"); // dacă ai status de genul ăsta; altfel scoate
            }
            reservationService.save(reservation);
        }
    }

    public List<SessionEntity> findTodayAll() {
        var range = getRange(SessionDayFilter.TODAY);
        return sessionRepository.findByScheduledDateGreaterThanEqualAndScheduledDateLessThan(
                range.start(), range.end()
        );
    }

    public List<SessionEntity> findTodayByCoach(Long coachId) {
        var range = getRange(SessionDayFilter.TODAY);

        List<SessionEntity> byReservation = sessionRepository
                .findByReservation_Coach_CoachIdAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
                        coachId, range.start(), range.end()
                );

        List<SessionEntity> bySubscription = sessionRepository
                .findBySubscription_Coach_CoachIdAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
                        coachId, range.start(), range.end()
                );

        //  după sessionId
        return java.util.stream.Stream.concat(byReservation.stream(), bySubscription.stream())
                .collect(java.util.stream.Collectors.toMap(
                        SessionEntity::getSessionId,
                        s -> s,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();
    }



}
