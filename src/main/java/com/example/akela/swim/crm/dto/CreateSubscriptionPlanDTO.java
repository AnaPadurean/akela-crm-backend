package com.example.akela.swim.crm.dto;

import lombok.Data;

@Data
public class CreateSubscriptionPlanDTO {
    private Long subscriptionTypeId;
    private Integer sessions;
    private Double price;
//    private Date validFrom;
//    private Date validTo;
    private boolean active;
}
