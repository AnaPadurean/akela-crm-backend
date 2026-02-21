package com.example.akela.swim.crm.graphs;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsGraphsController {

    private final PaymentsGraphsService graphsService;

    @GetMapping("/stats")
    public ResponseEntity<PaymentsStatsDTO> stats(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(graphsService.getStats(q, from, to));
    }
}
