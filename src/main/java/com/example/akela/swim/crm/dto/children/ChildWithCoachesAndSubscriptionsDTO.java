package com.example.akela.swim.crm.dto.children;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ChildWithCoachesAndSubscriptionsDTO {
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private Date birthday;

    private List<Map<String, Object>> coachChildren;
    private List<Map<String, Object>> subscriptions;

}
