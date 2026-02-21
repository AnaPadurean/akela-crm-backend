package com.example.akela.swim.crm.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Long reservationId;
    private Long coachId;
    private String coachFullName;
    private Long sessionId;
    private Long childId;
    private String childFullName;
    private LocalDateTime reservationDateTime;
    private String status;
    private Long subscriptionId;
    private String subscriptionName;
    private Long weeklyTrainingSessionId;


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public String getCoachFullName() {
        return coachFullName;
    }

    public void setCoachFullName(String coachFullName) {
        this.coachFullName = coachFullName;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getChildFullName() {
        return childFullName;
    }

    public void setChildFullName(String childFullName) {
        this.childFullName = childFullName;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Long getWeeklyTrainingSessionId() {
        return weeklyTrainingSessionId;
    }

    public void setWeeklyTrainingSessionId(Long weeklyTrainingSessionId) {
        this.weeklyTrainingSessionId = weeklyTrainingSessionId;
    }

}
