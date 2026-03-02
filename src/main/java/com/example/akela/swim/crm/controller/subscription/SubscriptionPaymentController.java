package com.example.akela.swim.crm.controller.subscription;

import com.example.akela.swim.crm.dto.CreatePaymentDTO;
import com.example.akela.swim.crm.dto.PaymentDTO;
import com.example.akela.swim.crm.entity.SubscriptionPaymentEntity;
import com.example.akela.swim.crm.service.subscriptions.SubscriptionPaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubscriptionPaymentController {

    private final SubscriptionPaymentService paymentService;

    public SubscriptionPaymentController(SubscriptionPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/subscriptions/{id}/payments")
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable("id") Long subscriptionId, @RequestBody CreatePaymentDTO dto) {
        SubscriptionPaymentEntity saved = paymentService.createPayment(subscriptionId, dto);

        var sub = saved.getSubscription();

        PaymentDTO res = new PaymentDTO();
        res.setPaymentId(saved.getPaymentId());
        res.setSubscriptionId(sub.getSubscriptionId());
        res.setAmount(saved.getAmount());
        res.setPaidDate(saved.getPaidDate());
        res.setObservations(saved.getObservations());

        res.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());

        res.setPlanName(sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe)");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getPayments(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<PaymentDTO> list = paymentService.getAll(from, to).stream().map(p -> {
            var sub = p.getSubscription();

            PaymentDTO dto = new PaymentDTO();
            dto.setPaymentId(p.getPaymentId());
            dto.setSubscriptionId(sub.getSubscriptionId());
            dto.setAmount(p.getAmount());
            dto.setPaidDate(p.getPaidDate());
            dto.setObservations(p.getObservations());

            dto.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());

            dto.setPlanName(sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe)");

            return dto;
        }).toList();

        return ResponseEntity.ok(list);
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable("id") Long paymentId, @RequestBody CreatePaymentDTO dto) {
        SubscriptionPaymentEntity updated = paymentService.updatePayment(paymentId, dto);

        var sub = updated.getSubscription();

        PaymentDTO res = new PaymentDTO();
        res.setPaymentId(updated.getPaymentId());
        res.setSubscriptionId(sub.getSubscriptionId());
        res.setAmount(updated.getAmount());
        res.setPaidDate(updated.getPaidDate());
        res.setObservations(updated.getObservations());

        res.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());

        res.setPlanName(sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe)");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable Long paymentId) {
        SubscriptionPaymentEntity p = paymentService.getById(paymentId);
        var sub = p.getSubscription();

        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(p.getPaymentId());
        dto.setSubscriptionId(sub.getSubscriptionId());
        dto.setAmount(p.getAmount());
        dto.setPaidDate(p.getPaidDate());
        dto.setObservations(p.getObservations());
        dto.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());
        dto.setPlanName(sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe)");
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/payments/search")
    public ResponseEntity<Page<PaymentDTO>> searchPayments(@RequestParam(required = false) String q,

                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

                                                           @PageableDefault(size = 25) Pageable pageable) {
        Page<PaymentDTO> page = paymentService.search(q, from, to, pageable).map(p -> {
            var sub = p.getSubscription();

            PaymentDTO dto = new PaymentDTO();
            dto.setPaymentId(p.getPaymentId());
            dto.setSubscriptionId(sub.getSubscriptionId());
            dto.setAmount(p.getAmount());
            dto.setPaidDate(p.getPaidDate());
            dto.setObservations(p.getObservations());

            dto.setChildFullName(sub.getChild().getChildLastName() + " " + sub.getChild().getChildFirstName());

            dto.setPlanName(sub.getPlan().getSubscriptionType().getName() + " (" + sub.getPlan().getSessions() + " ședințe)");

            return dto;
        });

        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") Long paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
