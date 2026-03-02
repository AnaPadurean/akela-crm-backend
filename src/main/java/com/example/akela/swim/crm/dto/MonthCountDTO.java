package com.example.akela.swim.crm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MonthCountDTO {
    private Integer month; // 1..12
    private Long count;

    public MonthCountDTO(Integer month, Long count) {
        this.month = month;
        this.count = count;
    }

}
