// src/main/java/com/example/akela/swim/crm/dto/PaymentsStatsDTO.java
package com.example.akela.swim.crm.graphs;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PaymentsStatsDTO {
    private String q;
    private LocalDate from;
    private LocalDate to;

    private double totalAmount;
    private long paymentsCount;

    private double avgPerDay;
    private LocalDate bestDay;
    private double bestDayAmount;

    private List<DailyPointDTO> daily;
    private List<TypePointDTO> byType;
    private PaymentsBucket bucket;
    private List<BucketPointDTO> series;
    private List<TypePointDTO> byTypeTop;

}
