package com.example.akela.swim.crm.graphs;

import java.time.LocalDate;

public interface DailyTotalProjection {
    LocalDate getDay();
    Double getTotal();
}
