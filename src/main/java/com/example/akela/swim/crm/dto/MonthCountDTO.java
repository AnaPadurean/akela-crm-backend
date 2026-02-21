package com.example.akela.swim.crm.dto;

public class MonthCountDTO {
    private Integer month; // 1..12
    private Long count;

    public MonthCountDTO(Integer month, Long count) {
        this.month = month;
        this.count = count;
    }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}
