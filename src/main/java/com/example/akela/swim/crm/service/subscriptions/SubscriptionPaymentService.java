package com.example.akela.swim.crm.service.subscriptions;

import com.example.akela.swim.crm.dto.CreatePaymentDTO;
import com.example.akela.swim.crm.entity.SubscriptionEntity;
import com.example.akela.swim.crm.entity.SubscriptionPaymentEntity;
import com.example.akela.swim.crm.repository.SubscriptionPaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPaymentService {

    private final SubscriptionPaymentRepository paymentRepo;
    private final SubscriptionService subscriptionService;

    public SubscriptionPaymentService(SubscriptionPaymentRepository paymentRepo,
                                      SubscriptionService subscriptionService) {
        this.paymentRepo = paymentRepo;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public SubscriptionPaymentEntity createPayment(Long subscriptionId, CreatePaymentDTO dto) {
        SubscriptionEntity sub = subscriptionService.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (paymentRepo.existsBySubscription_SubscriptionId(subscriptionId)) {
            throw new RuntimeException("Abonamentul este deja încasat.");
        }

        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new RuntimeException("Suma trebuie să fie > 0");
        }
        if (dto.getPaidDate() == null) {
            throw new RuntimeException("Data plății este obligatorie");
        }

        SubscriptionPaymentEntity p = new SubscriptionPaymentEntity();
        p.setSubscription(sub);
        p.setAmount(dto.getAmount());
        p.setPaidDate(dto.getPaidDate());

        SubscriptionPaymentEntity saved = paymentRepo.save(p);

        sub.setIsPaid(true);
        subscriptionService.save(sub);

        return saved;
    }


    public List<SubscriptionPaymentEntity> getAll(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return paymentRepo.findAllByPaidDateBetweenOrderByPaidDateDesc(from, to);
        }
        return paymentRepo.findAllByOrderByPaidDateDesc();
    }

    @Transactional
    public SubscriptionPaymentEntity updatePayment(Long paymentId, CreatePaymentDTO dto) {
        SubscriptionPaymentEntity p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if (dto.getAmount() != null) {
            p.setAmount(dto.getAmount());
        }
        if (dto.getPaidDate() != null) {
            p.setPaidDate(dto.getPaidDate());
        }

        if (dto.getObservations() != null) {
            p.setObservations(dto.getObservations());
        }

        return paymentRepo.save(p);
    }

    public Optional<SubscriptionPaymentEntity> findBySubscriptionId(Long subscriptionId) {
        return paymentRepo.findBySubscription_SubscriptionId(subscriptionId);
    }


    public SubscriptionPaymentEntity getById(Long paymentId) {
        return paymentRepo.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    public Page<SubscriptionPaymentEntity> search(
            String q,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    ) {
        if (q != null && q.isBlank()) {
            q = null;
        }
        return paymentRepo.search(q, from, to, pageable);
    }

    @Transactional
    public void deletePayment(Long paymentId) {
        SubscriptionPaymentEntity p = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        SubscriptionEntity sub = p.getSubscription();

        paymentRepo.delete(p);

        sub.setIsPaid(false);
        subscriptionService.save(sub);
    }




}
