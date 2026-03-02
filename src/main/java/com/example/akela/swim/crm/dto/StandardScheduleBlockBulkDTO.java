package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
public class StandardScheduleBlockBulkDTO {
    private Long coachId;
    private String reason;
    private List<BlockSlotDTO> slots;

    @Setter
    @Getter
    public static class BlockSlotDTO {
        private Integer dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

    }

}
