package com.example.akela.swim.crm.dto.subscription;

import lombok.Data;

@Data
public class SubscriptionTypeDTO {
    private Long subscriptionTypeId;
    private String code;
    private String name;
    private Integer maxChildren;
    private String description;
}
