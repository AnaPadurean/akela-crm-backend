package com.example.akela.swim.crm.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConfirmWeeklySessionDTO {
    private boolean confirmed;
    private List<ChildReservation> reservations;

    @Data
    public static class ChildReservation {
        private Long childId;
        private Long subscriptionId;
        private String status; // optional
    }
}

