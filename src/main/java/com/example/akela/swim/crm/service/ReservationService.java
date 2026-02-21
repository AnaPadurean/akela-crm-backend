package com.example.akela.swim.crm.service;

import com.example.akela.swim.crm.entity.ReservationEntity;
import com.example.akela.swim.crm.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationEntity> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<ReservationEntity> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public ReservationEntity save(ReservationEntity reservationEntity) {
        return reservationRepository.save(reservationEntity);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationEntity> filter(String start, String end, Long childId) {
        LocalDateTime startDate = start != null ? LocalDateTime.parse(start) : null;
        LocalDateTime endDate = end != null ? LocalDateTime.parse(end) : null;

        return reservationRepository.filter(startDate, endDate, childId);
    }

    public List<ReservationEntity> findByWeeklyTrainingSessionId(Long weeklyId) {
        return reservationRepository.findByWeeklyTrainingSession_Id(weeklyId);
    }

    public List<ReservationEntity> findBySubscriptionId(Long subscriptionId) {
        return reservationRepository
                .findBySubscription_SubscriptionIdOrderByReservationDateTimeDesc(subscriptionId);
    }





}
