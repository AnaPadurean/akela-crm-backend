package com.example.akela.swim.crm.dto.children;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChildWithCoachesAndSubscriptionsDTO {
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private Date birthday;

    private List<Map<String, Object>> coachChildren;
    private List<Map<String, Object>> subscriptions;

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getChildFirstName() {
        return childFirstName;
    }

    public void setChildFirstName(String childFirstName) {
        this.childFirstName = childFirstName;
    }

    public String getChildLastName() {
        return childLastName;
    }

    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Map<String, Object>> getCoachChildren() {
        return coachChildren;
    }

    public void setCoachChildren(List<Map<String, Object>> coachChildren) {
        this.coachChildren = coachChildren;
    }

    public List<Map<String, Object>> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Map<String, Object>> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
