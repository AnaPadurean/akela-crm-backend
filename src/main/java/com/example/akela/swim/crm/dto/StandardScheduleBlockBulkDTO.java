package com.example.akela.swim.crm.dto;

import java.time.LocalTime;
import java.util.List;

public class StandardScheduleBlockBulkDTO {
    private Long coachId;
    private String reason;
    private List<BlockSlotDTO> slots;

    public static class BlockSlotDTO {
        private Integer dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        public Integer getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    }

    public Long getCoachId() { return coachId; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public List<BlockSlotDTO> getSlots() { return slots; }
    public void setSlots(List<BlockSlotDTO> slots) { this.slots = slots; }
}
