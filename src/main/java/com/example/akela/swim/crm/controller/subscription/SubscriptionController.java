package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.CreateSubscriptionDTO;
import com.example.akela.swim.crm.dto.subscription.SubscriptionDTO;
import com.example.akela.swim.crm.entity.*;
import com.example.akela.swim.crm.service.children.ChildrenService;
import com.example.akela.swim.crm.service.coach.CoachService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionPaymentService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionPlanService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionService;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")

public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final ChildrenService childService;
    private final CoachService coachService;
    private final SubscriptionPlanService planService;
    private final SubscriptionPaymentService paymentService;
    private final SubscriptionStatsService subscriptionStatsService;


    public SubscriptionController(SubscriptionService subscriptionService, ChildrenService childService, CoachService coachService, SubscriptionPlanService planService, SubscriptionPaymentService paymentService, SubscriptionStatsService subscriptionStatsService) {
        this.subscriptionService = subscriptionService;
        this.childService = childService;
        this.coachService = coachService;
        this.planService = planService;
        this.paymentService = paymentService;
        this.subscriptionStatsService = subscriptionStatsService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAll() {
        List<SubscriptionDTO> dtos = subscriptionService.findAll().stream().map(s -> {
            SubscriptionDTO dto = new SubscriptionDTO();
            dto.setSubscriptionId(s.getSubscriptionId());
            dto.setChildFullName(s.getChild().getChildLastName() + " " + s.getChild().getChildFirstName());
            dto.setCoachFullName(s.getCoach() != null ? s.getCoach().getCoachLastName() + " " + s.getCoach().getCoachFirstName() : "—");
            dto.setPlanName(s.getPlan().getSubscriptionType().getName() + " (" + s.getPlan().getSessions() + " ședințe / " + s.getPlan().getPrice() + " lei)");


            Integer planSessions = s.getPlan() != null ? s.getPlan().getSessions() : 0;
            Integer total = (s.getTotalSessions() != null && s.getTotalSessions() > 0) ? s.getTotalSessions() : planSessions;
            Integer completed = (s.getCompletedSessions() != null) ? s.getCompletedSessions() : 0;
            Integer remaining = (s.getRemainingSessions() != null) ? s.getRemainingSessions() : (total - completed);

            dto.setTotalSessions(total);
            dto.setCompletedSessions(completed);
            dto.setRemainingSessions(remaining);
            dto.setStatus(s.getStatus() != null ? s.getStatus() : "ACTIV");
            dto.setIsPaid(s.getIsPaid());
            if (Boolean.TRUE.equals(s.getIsPaid())) {
                paymentService.findBySubscriptionId(s.getSubscriptionId()).ifPresent(p -> dto.setPaymentId(p.getPaymentId()));
            }

            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> getActiveSubscriptionsCount() {
        return ResponseEntity.ok(subscriptionStatsService.getActiveSubscriptionsCount());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SubscriptionDTO>> search(@RequestParam(required = false) List<Long> childIds, @RequestParam(required = false) String status,   // "ALL"/"ACTIV"/"INACTIVE"
                                                        @RequestParam(required = false) Boolean paid     // null=ALL, true, false
    ) {
        List<SubscriptionEntity> subs = subscriptionService.search(childIds, status, paid);

        List<SubscriptionDTO> dtos = subs.stream().map(s -> {
            SubscriptionDTO dto = new SubscriptionDTO();
            dto.setSubscriptionId(s.getSubscriptionId());
            dto.setChildFullName(s.getChild().getChildLastName() + " " + s.getChild().getChildFirstName());
            dto.setCoachFullName(s.getCoach() != null ? s.getCoach().getCoachLastName() + " " + s.getCoach().getCoachFirstName() : "—");
            dto.setPlanName(s.getPlan().getSubscriptionType().getName() + " (" + s.getPlan().getSessions() + " ședințe / " + s.getPlan().getPrice() + " lei)");

            Integer planSessions = s.getPlan() != null ? s.getPlan().getSessions() : 0;
            Integer total = (s.getTotalSessions() != null && s.getTotalSessions() > 0) ? s.getTotalSessions() : planSessions;
            Integer completed = (s.getCompletedSessions() != null) ? s.getCompletedSessions() : 0;
            Integer remaining = (s.getRemainingSessions() != null) ? s.getRemainingSessions() : (total - completed);

            dto.setTotalSessions(total);
            dto.setCompletedSessions(completed);
            dto.setRemainingSessions(remaining);
            dto.setStatus(s.getStatus() != null ? s.getStatus() : "ACTIV");
            dto.setIsPaid(Boolean.TRUE.equals(s.getIsPaid()));
            if (Boolean.TRUE.equals(s.getIsPaid())) {
                paymentService.findBySubscriptionId(s.getSubscriptionId()).ifPresent(p -> dto.setPaymentId(p.getPaymentId()));
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateSubscriptionDTO dto) {
        try {
            ChildrenEntity child = childService.findById(dto.getChildId()).orElseThrow(() -> new RuntimeException("Child not found"));
            SubscriptionPlanEntity plan = planService.findById(dto.getPlanId()).orElseThrow(() -> new RuntimeException("Plan not found"));
            CoachEntity coach = null;
            if (dto.getCoachId() != null) {
                coach = coachService.findById(dto.getCoachId()).orElseThrow(() -> new RuntimeException("Coach not found"));
            }

            List<SubscriptionEntity> activeSubs = subscriptionService.findActiveByChildIdAll(child.getChildId());
            for (SubscriptionEntity active : activeSubs) {
                if (active.getPlan().getSubscriptionType().getSubscriptionTypeId().equals(plan.getSubscriptionType().getSubscriptionTypeId())) {
                    active.setStatus("INACTIVE");
                    subscriptionService.save(active);
                }
            }

            SubscriptionEntity sub = new SubscriptionEntity();
            sub.setChild(child);
            sub.setCoach(coach);
            sub.setPlan(plan);
            sub.setTotalSessions(plan.getSessions());
            sub.setCompletedSessions(0);
            sub.setStatus("ACTIV");
            sub.setIsPaid(false);

            // 🔹 Dacă există deficit (remaining < 0) pe un abonament anterior, îl aplicăm pe abonamentul nou (indiferent de tip)
            // și actualizăm abonamentul vechi (ex: -20 devine -12 după ce consumăm 8)
            subscriptionService.findMostRecentNegativeByChildId(child.getChildId()).ifPresent(negSub -> {
                Integer prevRemaining = negSub.getRemainingSessions(); // ex: -20
                if (prevRemaining != null && prevRemaining < 0) {
                    int deficit = Math.abs(prevRemaining);             // 20
                    int planSessions = plan.getSessions() != null ? plan.getSessions() : 0;

                    int applied = Math.min(deficit, planSessions);     // cât putem acoperi din noul plan
                    int newCompleted = applied;
                    int newRemaining = planSessions - applied;

                    // abonamentul nou începe cu ședințe deja "consumate"
                    sub.setCompletedSessions(newCompleted);
                    sub.setRemainingSessions(newRemaining);

                    // actualizăm vechiul abonament: -20 + 8 = -12 (deficit rămas)
                    negSub.setRemainingSessions(prevRemaining + applied);

                    // opțional: dacă ajunge la 0, îl aducem exact la 0 (pentru curățenie)
                    if (negSub.getRemainingSessions() != null && negSub.getRemainingSessions() > 0) {
                        negSub.setRemainingSessions(0);
                    }

                    subscriptionService.save(negSub);
                }
            });

            // fallback dacă nu a setat nimeni remaining
            if (sub.getRemainingSessions() == null) {
                sub.setRemainingSessions(plan.getSessions());
            }


            SubscriptionEntity saved = subscriptionService.save(sub);
            SubscriptionDTO response = new SubscriptionDTO();
            response.setSubscriptionId(saved.getSubscriptionId());
            response.setChildFullName(child.getChildLastName() + " " + child.getChildFirstName());
            response.setCoachFullName(coach != null ? coach.getCoachLastName() + " " + coach.getCoachFirstName() : "—");
            response.setPlanName(plan.getSubscriptionType().getName() + " (" + plan.getSessions() + " ședințe / " + plan.getPrice() + " lei)");
            response.setTotalSessions(saved.getTotalSessions());
            response.setCompletedSessions(saved.getCompletedSessions());
            response.setRemainingSessions(saved.getRemainingSessions());
            response.setStatus(saved.getStatus());
            response.setIsPaid(saved.getIsPaid());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Eroare la salvare: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateSubscriptionDTO dto) {
        try {
            SubscriptionEntity existing = subscriptionService.findById(id).orElseThrow(() -> new RuntimeException("Subscription not found"));

            ChildrenEntity child = childService.findById(dto.getChildId()).orElseThrow(() -> new RuntimeException("Child not found"));
            SubscriptionPlanEntity plan = planService.findById(dto.getPlanId()).orElseThrow(() -> new RuntimeException("Plan not found"));

            CoachEntity coach = null;
            if (dto.getCoachId() != null) {
                coach = coachService.findById(dto.getCoachId()).orElseThrow(() -> new RuntimeException("Coach not found"));
            }

            Long oldPlanId = existing.getPlan() != null ? existing.getPlan().getSubscriptionPlanId() : null;

            existing.setPlan(plan);

            if (oldPlanId == null || !oldPlanId.equals(plan.getSubscriptionPlanId())) {
                existing.setTotalSessions(plan.getSessions());
                int completed = existing.getCompletedSessions() != null ? existing.getCompletedSessions() : 0;
                existing.setRemainingSessions(plan.getSessions() - completed);
            }

            if (dto.getIsPaid() != null) {
                existing.setIsPaid(dto.getIsPaid());
            }

            if (dto.getStatus() != null) existing.setStatus(dto.getStatus());

            SubscriptionEntity updated = subscriptionService.save(existing);

            SubscriptionDTO response = new SubscriptionDTO();
            response.setSubscriptionId(updated.getSubscriptionId());
            response.setChildFullName(child.getChildLastName() + " " + child.getChildFirstName());
            response.setCoachFullName(coach != null ? coach.getCoachLastName() + " " + coach.getCoachFirstName() : "—");
            response.setPlanName(plan.getSubscriptionType().getName() + " (" + plan.getSessions() + " ședințe / " + plan.getPrice() + " lei)");
            response.setTotalSessions(updated.getTotalSessions());
            response.setCompletedSessions(updated.getCompletedSessions());
            response.setRemainingSessions(updated.getRemainingSessions());
            response.setStatus(updated.getStatus());
            response.setIsPaid(updated.getIsPaid());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Eroare la actualizare: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
