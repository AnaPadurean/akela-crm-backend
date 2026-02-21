package com.example.akela.swim.crm.dto.subscription;

import lombok.Data;

@Data
public class SubscriptionPlanDTO {
    private Long subscriptionPlanId;
    private String subscriptionTypeName;
    private Long subscriptionTypeId;
    private Integer sessions;
    private Double price;
//    private Date validFrom;
//    private Date validTo;
    private boolean active;
}
